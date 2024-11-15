package com.example.demo_oracle_db.service.product.impl;

import com.example.demo_oracle_db.config.ValidatorHandler;
import com.example.demo_oracle_db.entity.Product;
import com.example.demo_oracle_db.entity.Voucher;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.*;
import com.example.demo_oracle_db.service.excelParse.ExcelParse;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.excelParse.response.ParseResult;
import com.example.demo_oracle_db.service.excelParse.response.ProductImportData;
import com.example.demo_oracle_db.service.product.ProductService;
import com.example.demo_oracle_db.service.product.request.AddVoucherRequest;
import com.example.demo_oracle_db.service.product.request.ProductFilter;
import com.example.demo_oracle_db.service.product.request.ProductRequest;
import com.example.demo_oracle_db.service.product.response.ProductDto;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductVoucherRepository productVoucherRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    ValidatorHandler<ProductRequest> validatorHandler;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ExcelParse<ProductImportData> excelParse;

    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Page<ProductDto> getProducts(ProductFilter productFilter) {
        int page = productFilter.getPageNum() != null ? productFilter.getPageNum() : 1;
        int size = productFilter.getSizePage() != null ? productFilter.getSizePage() : 8;

        Page<Product> products = productRepository.findAll((Specification<Product>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productFilter.getName() != null && !productFilter.getName().isBlank()) {
                String keyword = productFilter.getName().toLowerCase().trim();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword + "%"));
            }
            if (productFilter.getYearMaking() != null) {
                predicates.add(criteriaBuilder.equal(root.get("yearMaking"), productFilter.getYearMaking()));
            }
            if (productFilter.getStartExpireDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expireDate"), LocalDate.parse(productFilter.getStartExpireDate(), dateTimeFormat)));
            }
            if (productFilter.getEndExpireDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expireDate"), LocalDate.parse(productFilter.getEndExpireDate(), dateTimeFormat)));
            }
            if (productFilter.getMinQuantity() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), productFilter.getMinQuantity()));
            }
            if (productFilter.getMaxQuantity() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), productFilter.getMaxQuantity()));
            }
            if (productFilter.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), productFilter.getMinPrice()));
            }
            if (productFilter.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), productFilter.getMaxPrice()));
            }

            if (productFilter.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("category").get("id"), productFilter.getCategoryId()));
            }
            if (productFilter.getSupplierId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("supplier").get("id"), productFilter.getSupplierId()));
            }
            // Voucher code filtering
            if (productFilter.getVoucherCode() != null && !productFilter.getVoucherCode().isBlank()) {
                String keyword = productFilter.getVoucherCode().toLowerCase();
                predicates.add(criteriaBuilder.like(criteriaBuilder
                        .lower(root.join("productVouchers", JoinType.LEFT)
                                .join("voucher", JoinType.LEFT)
                                .get("code")), "%" + keyword + "%"));
            }

            if (productFilter.getCustomerId() != null) {
                predicates.add(criteriaBuilder.
                        equal(root.join("orderDetails", JoinType.LEFT)
                                .join("order", JoinType.LEFT)
                                .join("customer", JoinType.LEFT)
                                .get("id"), productFilter.getCustomerId()));
            }

            if (productFilter.getOrderCol() != null && !productFilter.getOrderCol().isBlank()) {
                assert query != null;
                if (productFilter.getSortDesc() != null ? productFilter.getSortDesc() : false) {
                    query.orderBy(criteriaBuilder.desc(root.get(productFilter.getOrderCol())));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(productFilter.getOrderCol())));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page - 1, size));
        return ProductDto.convertToProductDtoPage(products);
    }

    @Override
    public void addProduct(ProductRequest request) throws DodException {
        try {
            productRepository.addProduct(
                    request.getName(),
                    request.getYearMaking(),
                    LocalDate.parse(request.getExpireDate(), dateTimeFormat),
                    request.getQuantity(),
                    request.getPrice(),
                    request.getCategoryId(),
                    request.getSupplierId()
            );
        } catch (Exception e) {
            throw new DodException(MessageCode.ADD_PRODUCT_FAILED);
        }
    }

    @Override
    public void updateProduct(ProductRequest request, int option) throws DodException {
        if (!productRepository.existsById(request.getId())) {
            throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        }
        try {
            productRepository.updateProduct(
                    request.getId(),
                    request.getName(),
                    request.getYearMaking(),
                    LocalDate.parse(request.getExpireDate(), dateTimeFormat),
                    request.getQuantity(),
                    request.getPrice(),
                    request.getCategoryId(),
                    request.getSupplierId()
            );
        } catch (Exception e) {
            throw new DodException(MessageCode.UPDATE_PRODUCT_FAILED, request.getId(),null);
        }
    }

    @Override
    public void deleteProduct(Long id) throws DodException {
        if (productRepository.existsById(id)) {
            try {
                productRepository.deleteById(id);
            } catch (Exception e) {
                throw new DodException(MessageCode.DELETE_PRODUCT_FAILED);
            }
        } else {
            throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        }
    }

    @Override
    @Transactional
    public ParseResult<ProductImportData> importProducts(ParseResult<ProductImportData> requests) {
        for (ProductImportData request : requests.getListData()) {
            if (!request.isFail()) {
                try {
                    switch (requests.getTypeImport()) {
                        case Constants.TypeImport.ADD -> handleAdd(request);
                        case Constants.TypeImport.UPDATE -> handleUpdate(request);
                        case Constants.TypeImport.DELETE -> handleDelete(request);
                    }
                    request.setSuccess(true);
                } catch (Exception e) {
                    request.setSuccess(false);
                }
            }
        }
        requests.setSuccess(true);
        return requests;
    }

    private void handleAdd(ProductImportData request) throws DodException {
        if (productRepository.existsByName(request.getName())) {
            throw new DodException(MessageCode.PRODUCT_NAME_EXIST);
        }
        Long categoryId = categoryRepository.getIdByName(request.getCategoryName())
                .orElseThrow(() -> new DodException(MessageCode.CATEGORY_NOT_EXIST));
        Long supplierId = supplierRepository.getIdByName(request.getSupplierName())
                .orElseThrow(() -> new DodException(MessageCode.SUPPLIER_NOT_EXIST));
        productRepository.addProduct(
                request.getName(),
                request.getYearMaking(),
                request.getExpireDate(),
                request.getQuantity(),
                request.getPrice(),
                categoryId,
                supplierId
        );
    }

    private void handleUpdate(ProductImportData request) throws DodException {
        if (!productRepository.existsById(request.getId())) {
            throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        }
        if (productRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DodException(MessageCode.PRODUCT_NAME_EXIST);
        }
        Long categoryId = categoryRepository.getIdByName(request.getCategoryName())
                .orElseThrow(() -> new DodException(MessageCode.CATEGORY_NOT_EXIST));
        Long supplierId = supplierRepository.getIdByName(request.getSupplierName())
                .orElseThrow(() -> new DodException(MessageCode.SUPPLIER_NOT_EXIST));
        productRepository.updateProduct(
                request.getId(),
                request.getName(),
                request.getYearMaking(),
                request.getExpireDate(),
                request.getQuantity(),
                request.getPrice(),
                categoryId,
                supplierId
        );
    }

    private void handleDelete(ProductImportData request) throws DodException {
        if (!productRepository.existsById(request.getId())) {
            throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        }
        if (orderDetailRepository.existsByProductId(request.getId())) {
            throw new DodException(MessageCode.PRODUCT_IN_ORDER);
        }
        productVoucherRepository.deleteByProductId(request.getId());
        productRepository.deleteById(request.getId());
    }


    @Override
    public Page<Product> getProductsProcedure(ProductFilter filter) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GETPRODUCTLIST")
                .registerStoredProcedureParameter("pro_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("year_making", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("start_expri_date", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("end_expri_date", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("min_quality", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("max_quality", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("min_price", Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter("max_price", Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter("order_col", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("sort_desc", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("product", void.class, ParameterMode.REF_CURSOR)

                .setParameter("pro_name", filter.getName())
                .setParameter("year_making", filter.getYearMaking())
                .setParameter("start_expri_date", filter.getStartExpireDate())
                .setParameter("end_expri_date", filter.getEndExpireDate())
                .setParameter("min_quality", filter.getMinQuantity())
                .setParameter("max_quality", filter.getMaxQuantity())
                .setParameter("min_price", filter.getMinPrice())
                .setParameter("max_price", filter.getMaxPrice())
                .setParameter("order_col", filter.getOrderCol())
                .setParameter("sort_desc", (filter.getSortDesc() != null && filter.getSortDesc()) ? 1 : 0);//KHông nhận kiểu Boolean

        query.execute();

        List<Product> result = query.getResultList();

        // Tính toán tổng số trang
        long totalElements = result.size();// Lấy tổng số sản phẩm từ cơ sở dữ liệu (có thể viết một query riêng cho việc này)
        int page = filter.getPageNum() != null ? filter.getPageNum() : 1;
        int size = filter.getSizePage() != null ? filter.getSizePage() : 8;
        int start = Math.toIntExact((long) (page - 1) * size);
        int end = Math.min(start + size, result.size());

        List<Product> paginatedProducts = result.subList(start, end);

        return new PageImpl<>(paginatedProducts, PageRequest.of(page - 1, size), totalElements);
    }


    @Override
    public void addVoucherToProduct(AddVoucherRequest request) throws DodException {
        if (!productRepository.existsById(request.getProductId())) {
            throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        }
        Voucher voucher = voucherRepository.findByCode(request.getVoucherCode()).orElseThrow(() -> new DodException(MessageCode.VOUCHER_CODE_NOT_EXIST, request.getVoucherCode()));

        if (voucher.getEndDate().before(new Date())) {
            throw new DodException(MessageCode.VOUCHER_EXPIRED, voucher.getEndDate());
        }
        if (productVoucherRepository.existsByProductIdAndVoucherId(request.getProductId(), voucher.getId())) {
            throw new DodException(MessageCode.VOUCHER_ALREADY_ADD);
        }
        try {
            productVoucherRepository.addProductVoucher(request.getProductId(), voucher.getId());
        } catch (Exception ex) {
            throw new DodException(MessageCode.ADD_VOUCHER_FAILED);
        }

    }

    @Override
    public void deleteVoucherFromProduct(Long productId, Long voucherId) throws DodException {
        if (!productRepository.existsById(productId)) throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        if (!voucherRepository.existsById(voucherId)) throw new DodException(MessageCode.VOUCHER_NOT_EXIST);
        if (!productVoucherRepository.existsByProductIdAndVoucherId(productId, voucherId))
            throw new DodException(MessageCode.VOUCHER_PRODUCT_NOT_FOUND);
        try {
            productVoucherRepository.deleteByProductIdAndVoucherId(productId, voucherId);
        } catch (Exception ex) {
            throw new DodException(MessageCode.DELETE_VOUCHER_FAILED);
        }
    }

    @Override
    public ByteArrayInputStream productListReport() throws IOException {
        List<ProductDto> products = ((List<Product>) productRepository.findAll()).stream().map(ProductDto::new).toList();

        String[] columns = {"No", "Id","Name", "Year Making", "Price", "Quantity", "Expire Date", "Category", "Supplier"};
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet();

            sheet.setColumnWidth(0, 5 * 256);
            sheet.setColumnWidth(1, 5 * 256);
            sheet.setColumnWidth(2, 30 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 15 * 256);
            sheet.setColumnWidth(5, 10 * 256);
            sheet.setColumnWidth(6, 15 * 256);
            sheet.setColumnWidth(7, 20 * 256);
            sheet.setColumnWidth(8, 25 * 256);

            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, products.size(), 1, columns.length - 1));


            AreaReference areaReference = new AreaReference(
                    new CellReference(0, 0),
                    new CellReference(products.size()+1, columns.length - 1),
                    workbook.getSpreadsheetVersion()
            );
            XSSFTable table = sheet.createTable(areaReference);

            CTTable ctTable = table.getCTTable();
            ctTable.setRef(areaReference.formatAsString());
            ctTable.setDisplayName("MYTABLE");
            ctTable.setName("Test");
            ctTable.setId(1L);

            CTTableStyleInfo tableStyleInfo = ctTable.addNewTableStyleInfo();
            tableStyleInfo.setName("TableStyleLight1");
            tableStyleInfo.setShowColumnStripes(false);
            tableStyleInfo.setShowRowStripes(true);
            tableStyleInfo.setShowLastColumn(false);


            CTTableColumns tableColumns = ctTable.addNewTableColumns();

            tableColumns.setCount(columns.length);
            for (int i = 0; i < columns.length; i++) {
                CTTableColumn column = tableColumns.addNewTableColumn();
                column.setName(columns[i]);
                column.setId(i + 1);
            }

            XSSFRow header = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                XSSFCell cell = header.createCell(col);
                cell.setCellValue(columns[col]);
            }

            XSSFCellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd"));

            XSSFCellStyle integerStyle = workbook.createCellStyle();
            integerStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0"));

            XSSFCellStyle priceStyle = workbook.createCellStyle();
            priceStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("$#,##0.00"));

            int rowIndex = 1;
            for (ProductDto productDto : products) {
                XSSFRow row = sheet.createRow(rowIndex);

                XSSFCell noCell = row.createCell(0);
                noCell.setCellValue(rowIndex);
                noCell.setCellStyle(integerStyle);

                XSSFCell idCell = row.createCell(1);
                idCell.setCellValue(productDto.getId());
                idCell.setCellStyle(integerStyle);

                row.createCell(2).setCellValue(productDto.getName()); // Cột tên sản phẩm

                XSSFCell yearMakingCell = row.createCell(3);
                yearMakingCell.setCellValue(productDto.getYearMaking());
                yearMakingCell.setCellStyle(integerStyle);

                XSSFCell priceCell = row.createCell(4);
                priceCell.setCellValue(productDto.getPrice());
                // Áp dụng kiểu dữ liệu cho cột Price
                priceCell.setCellStyle(priceStyle);

                XSSFCell quantityCell = row.createCell(5);
                quantityCell.setCellValue(productDto.getQuantity());
                // Áp dụng kiểu dữ liệu cho cột Quantity
                quantityCell.setCellStyle(integerStyle);

                XSSFCell expireDateCell = row.createCell(6);
                if (productDto.getExpireDate() != null) {
                    expireDateCell.setCellValue(java.sql.Date.valueOf(productDto.getExpireDate()));
                    // Áp dụng kiểu dữ liệu cho cột Expire Date
                    expireDateCell.setCellStyle(dateStyle);
                }

                row.createCell(7).setCellValue(productDto.getCategoryName());
                row.createCell(8).setCellValue(productDto.getSupplierName());

                rowIndex++;
            }
            sheet.createRow(rowIndex);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ParseResult<ProductImportData> parseProduct(MultipartFile file, ParseOptions options) {
        return excelParse.parseExcel(file,options);
    }
}
