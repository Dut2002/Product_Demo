package com.example.demo_oracle_db.service.product.impl;

import com.example.demo_oracle_db.config.ValidatorHandler;
import com.example.demo_oracle_db.entity.Product;
import com.example.demo_oracle_db.entity.ProductVoucher;
import com.example.demo_oracle_db.entity.Voucher;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.ProductRepository;
import com.example.demo_oracle_db.repository.ProductVoucherRepository;
import com.example.demo_oracle_db.repository.VoucherRepository;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private ProductVoucherRepository productVoucherRepository;

    @Autowired
    ValidatorHandler<ProductRequest> validatorHandler;

    @Autowired
    private EntityManager entityManager;

    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Page<ProductDto> getProducts(ProductFilter productFilter) {
        int page = productFilter.getPageNum() != null ? productFilter.getPageNum() : 1;
        int size = productFilter.getSizePage() != null ? productFilter.getSizePage() : 8;

        Page<Product> products = productRepository.findAll((Specification<Product>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productFilter.getName() != null && !productFilter.getName().isBlank()) {
                String keyword = productFilter.getName().toLowerCase();
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
                if (productFilter.getSortDesc() != null ? productFilter.getSortDesc() : false) {
                    assert query != null;
                    query.orderBy(criteriaBuilder.desc(root.get(productFilter.getOrderCol())));
                }
                else {
                    assert query != null;
                    query.orderBy(criteriaBuilder.asc(root.get(productFilter.getOrderCol())));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page - 1, size));
        return ProductDto.convertToProductDtoPage(products);
    }

    @Override
    public Product getProduct(Long id) throws DodException {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
        }
        return product;
    }

    @Override
    public void addProduct(ProductRequest request, int option) throws DodException {
        Product product = new Product();
        try {
            saveProduct(product, request);
            productRepository.save(product);
        } catch (Exception e) {
            if (option == 0) {
                request.setStatus(Constants.ApiStatus.FAILED);
                request.setErrorMessage(MessageCode.ADD_PRODUCT_FAILED.getCode());
            } else {
                throw new DodException(MessageCode.ADD_PRODUCT_FAILED);
            }
        }
    }

    @Override
    public void updateProduct(ProductRequest request, int option) throws DodException {
        Product product = productRepository.findById(request.getId()).orElse(null);
        if (product == null) {
            if (option == 0) {
                request.setStatus(Constants.ApiStatus.FAILED);
                request.setErrorMessage(MessageCode.PRODUCT_NOT_EXIST.getCode());
                return;
            } else {
                throw new DodException(MessageCode.PRODUCT_NOT_EXIST);
            }
        }

        try {
            saveProduct(product, request);
            productRepository.save(product);
            request.setStatus(Constants.ApiStatus.SUCCESS);
        } catch (Exception e) {
            if (option == 0) {
                request.setStatus(Constants.ApiStatus.FAILED);
                request.setErrorMessage(MessageCode.UPDATE_PRODUCT_FAILED.getCode());
            } else {
                throw new DodException(MessageCode.UPDATE_PRODUCT_FAILED, request.getId());
            }
        }
    }

    private void saveProduct(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setYearMaking(request.getYearMaking());
        product.setExpireDate(LocalDate.parse(request.getExpireDate(), dateTimeFormat));
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setCategoryId(request.getCategoryId());
        product.setSupplierId(request.getSupplierId());
    }

    @Override
    public void deleteProduct(Long id) throws DodException {
        if (productRepository.findById(id).isPresent()) {
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
    public List<ProductRequest> importProducts(List<ProductRequest> requests) throws DodException {
        for (ProductRequest request : requests) {
            Map<String, String> errors = validatorHandler.validateRequest(request);
            if (!errors.isEmpty()) {
                request.setStatus(Constants.ApiStatus.FAILED);
                request.setErrorMessage(errors);
                continue;
            }

            if (request.getId() != null) {
                updateProduct(request, 0);
            } else {
                addProduct(request, 0);
            }
        }
        return requests;
    }

    @Override
    public List<Product> exportProduct(String search, Long yearMaking, LocalDate startExpireDate, LocalDate endExpireDate, Long startQuality, Long endQuality, Double startPrice, Double endPrice) {
        List<Product> products = productRepository.findAll((Specification<Product>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("PRONAME"), "%" + search + "%"));
            }
            if (yearMaking != null) {
                predicates.add(criteriaBuilder.equal(root.get("YEARMAKING"), yearMaking));
            }
            if (startExpireDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("EXPRIDATE"), startExpireDate));
            }
            if (endExpireDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("EXPRIDATE"), endExpireDate));
            }
            if (startQuality != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("QUALITY"), startQuality));
            }
            if (endQuality != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("QUALITY"), endQuality));
            }
            if (startPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("PRICE"), startPrice));
            }
            if (endPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("PRICE"), endPrice));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return products;
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
            ProductVoucher productVoucher = new ProductVoucher();
            productVoucher.setProductId(request.getProductId());
            productVoucher.setVoucherId(voucher.getId());
            productVoucherRepository.save(productVoucher);
        } catch (Exception ex) {
            throw new DodException(MessageCode.ADD_VOUCHER_FAILED);
        }

    }

    @Override
    public void deleteVoucherFromProduct(Long productId, Long voucherId) throws DodException {
        ProductVoucher productVoucher = productVoucherRepository.findByProductIdAndVoucherId(productId, voucherId).orElseThrow(() -> new DodException(MessageCode.VOUCHER_PRODUCT_NOT_FOUND));
        try {
            productVoucherRepository.deleteById(productVoucher.getId());
        } catch (Exception ex) {
            throw new DodException(MessageCode.DELETE_VOUCHER_FAILED);
        }
    }

    @Override
    public ByteArrayInputStream productListReport() throws IOException {
        List<ProductDto> products = ((List<Product>) productRepository.findAll()).stream().map(ProductDto::new).toList();

        String[] columns = {"No", "Name", "Year Making", "Price", "Quantity", "Expire Date", "Category", "Supplier"};
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CreationHelper creationHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Products");
            sheet.autoSizeColumn(columns.length);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++
            ) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(cellStyle);
            }
            CellStyle cellStyle1 = workbook.createCellStyle();
            cellStyle1.setDataFormat(creationHelper.createDataFormat().getFormat("#"));
            // Định dạng cho cột ngày (dd/MM/yyyy)
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            // Định dạng cho cột giá (tiền tệ USD)
            CellStyle priceCellStyle = workbook.createCellStyle();
            priceCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("$#,##0.00"));


            int rowIndex = 1;
            for (ProductDto productDto : products
            ) {
                Row row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(rowIndex++);
                row.createCell(1).setCellValue(productDto.getName());
                row.createCell(2).setCellValue(productDto.getYearMaking());
                row.createCell(3).setCellValue(productDto.getPrice());
                row.createCell(3).setCellStyle(priceCellStyle);
                row.createCell(4).setCellValue(productDto.getQuantity());
                row.createCell(5).setCellValue(productDto.getExpireDate());
                row.createCell(5).setCellStyle(dateCellStyle);
                row.createCell(6).setCellValue(productDto.getCategoryName());
                row.createCell(7).setCellValue(productDto.getSupplierName());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
