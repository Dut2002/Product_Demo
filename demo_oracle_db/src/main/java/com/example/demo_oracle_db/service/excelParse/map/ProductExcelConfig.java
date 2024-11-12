package com.example.demo_oracle_db.service.excelParse.map;

import com.example.demo_oracle_db.repository.CategoryRepository;
import com.example.demo_oracle_db.repository.ProductRepository;
import com.example.demo_oracle_db.repository.SupplierRepository;
import com.example.demo_oracle_db.service.excelParse.impl.ExcelMapper;
import com.example.demo_oracle_db.service.excelParse.response.HeaderMapping;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.excelParse.response.ProductImportData;
import com.example.demo_oracle_db.util.Constants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.*;

@Configuration
public class ProductExcelConfig extends ExcelConfig {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private Validator validator;


    @Bean
    public ExcelMapper<ProductImportData> productExcelMapper() {
        List<HeaderMapping<ProductImportData>> mappings = new ArrayList<>();

        // Mapping cho từng trường của Product
        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Id")
                .required(List.of(false,true,true))
                .converter(this::getLongValue)
                .setter((productImportData, id) -> productImportData.setId((Long) id))
                .unique(List.of(false, true, true))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Name")
                .required(List.of(true,true,false))
                .converter(this::getStringValue)
                .setter((productImportData, name) -> productImportData.setName((String) name))
                .unique(List.of(true,true,false))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Year Making")
                .required(List.of(true, true, false))
                .converter(this::getLongValue)
                .setter((productImportData, yearMaking) -> productImportData.setYearMaking((Long) yearMaking))
                .unique(List.of(false,false,false))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Expire Date")
                .required(List.of(false, false, false))
                .converter(this::getLocalDateValue)
                .unique(List.of(false,false,false))
                .setter((productImportData, expireDate) -> productImportData.setExpireDate((LocalDate) expireDate))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Quantity")
                .required(List.of(true, true, false))
                .converter(this::getIntegerValue)
                .unique(List.of(false, false, false))
                .setter((productImportData, quantity) -> productImportData.setQuantity((Integer) quantity))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Price")
                .required(List.of(true, true, false))
                .converter(this::getDoubleValue)
                .unique(List.of(false, false, false))
                .setter((productImportData, price) -> productImportData.setPrice((Double) price))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Category")
                .required(List.of(true, true, false))
                .converter(this::getStringValue)
                .unique(List.of(false, false, false))
                .setter((productImportData, categoryName) -> productImportData.setCategoryName((String) categoryName))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Supplier")
                .required(List.of(true, true, false))
                .converter(this::getStringValue)
                .unique(List.of(false, false, false))
                .setter((productImportData, supplierName) -> productImportData.setSupplierName((String) supplierName))
                .build());

        return new ExcelMapper<>(ProductImportData.class, mappings) {
            @Override
            protected List<String> validateRequiredColumns(Map<String, Integer> headerIndexMap, ParseOptions options) {
                List<HeaderMapping<ProductImportData>> requiredMappings = switch (options.getTypeImport()) {
                    case Constants.TypeImport.ADD ->
                        // Khi ADD: lọc ra các cột required và bỏ qua cột Id
                            mappings.stream()
                                    .filter(header -> !header.getHeaderName().equals("Id"))
                                    .toList();
                    case Constants.TypeImport.DELETE ->
                        // Khi DELETE: chỉ lấy cột Id
                            mappings.stream()
                                    .filter(header -> header.getHeaderName().equals("Id"))
                                    .toList();
                    default ->
                        // Các trường hợp khác (UPDATE): lấy tất cả cột required
                            mappings.stream()
                                    .toList();
                };
                List<String> missingColumn = new ArrayList<>();
                Map<String, Integer> header = new HashMap<>();
                for (HeaderMapping<ProductImportData> mapping : requiredMappings
                ) {
                    if (headerIndexMap.containsKey(mapping.getHeaderName())) {
                        header.put(mapping.getHeaderName(), headerIndexMap.get(mapping.getHeaderName()));
                    } else if (mapping.getRequired().get(options.getTypeImport()    ) || options.getStrictMapping()) {
                        missingColumn.add(mapping.getHeaderName());
                    }
                }
                headerIndexMap.clear();
                headerIndexMap.putAll(header);
                return missingColumn;
            }

            @Override
            protected void setFailed(ProductImportData item) {
                item.setSuccess(false);
            }

            @Override
            protected List<String> checkUnique(Object value, Set<Object> uniqueValues, HeaderMapping<ProductImportData> mapping, int rowNum, Integer typeImport) {
                List<String> errors = new ArrayList<>();

                if (!uniqueValues.add(value)) {
                    errors.add(
                            String.format("Giá trị '%s' của cột '%s' dòng '%s' đã tồn tại trong file Excel",
                                    value, mapping.getHeaderName(), rowNum)
                    );
                }
                return errors;
            }

            @Override
            public List<String> validateItem(ProductImportData item, int rowNum, Integer typeImport) {
                List<String> errors = new ArrayList<>();
                if(!typeImport.equals(Constants.TypeImport.DELETE)){
                    Set<ConstraintViolation<ProductImportData>> violations = validator.validate(item);
                    errors.addAll(violations.stream()
                            .map(violation -> "Dòng " + rowNum + " " + violation.getPropertyPath() + " " + violation.getMessage())
                            .toList());

                    if (!categoryRepository.existsByName(item.getCategoryName())) {
                        errors.add("Dòng " + rowNum + " category " + item.getCategoryName() + " không tồn tại");
                    }
                    if (!supplierRepository.existsByName(item.getSupplierName())) {
                        errors.add("Dòng " + rowNum + " supplier " + item.getSupplierName() + " không tồn tại");
                    }
                    if(item.getName()!=null){
                        if(typeImport.equals(Constants.TypeImport.UPDATE)){
                            if (productRepository.existsByNameAndIdNot(item.getName(),item.getId())) {
                                errors.add("Dòng " + rowNum + " product name " + item.getName() + " đã tồn tại ở sản phẩm khác");
                            }
                        }else{
                            if (productRepository.existsByName(item.getName())) {
                                errors.add("Dòng " + rowNum + " product name " + item.getName() + " đã tồn tại ở sản phẩm khác");
                            }
                        }
                    }
                }
                if(item.getId()!= null && !productRepository.existsById(item.getId())){
                    errors.add("Dòng " + rowNum + " id sản phẩm " + item.getId() + " không tồn tại");
                }
                return errors;
            }
        };
    }
}
