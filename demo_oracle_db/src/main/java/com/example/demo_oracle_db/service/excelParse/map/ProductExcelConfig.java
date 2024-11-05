package com.example.demo_oracle_db.service.excelParse.map;

import com.example.demo_oracle_db.repository.ProductRepository;
import com.example.demo_oracle_db.service.excelParse.impl.ExcelMapper;
import com.example.demo_oracle_db.service.excelParse.response.HeaderMapping;
import com.example.demo_oracle_db.service.excelParse.response.ProductImportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProductExcelConfig extends ExcelConfig {
    @Autowired
    private ProductRepository repository;

    @Bean
    public ExcelMapper<ProductImportData> productExcelMapper() {
        List<HeaderMapping<ProductImportData>> mappings = new ArrayList<>();

        // Mapping cho từng trường của Product
        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Id")
                .required(true)
                .converter(this::getLongValue)
                .setter((productImportData, id) -> productImportData.setId((Long) id))
                .unique(true)
                .exists(id -> repository.existsById((Long) id))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Name")
                .required(true)
                .converter(this::getStringValue)
                .setter((productImportData, name) -> productImportData.setName((String) name))
                .unique(true)
                .exists(name -> repository.existsByName((String) name))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Year Making")
                .required(true)
                .converter(this::getLongValue)
                .setter((productImportData, yearMaking) -> productImportData.setYearMaking((Long) yearMaking))
                .unique(false)
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Expire Date")
                .required(false)
                .converter(this::getLocalDateValue)
                .unique(false)
                .setter((productImportData, expireDate) -> productImportData.setExpireDate((LocalDate) expireDate))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Quantity")
                .required(false)
                .converter(this::getIntegerValue)
                .unique(false)
                .setter((productImportData, quantity) -> productImportData.setQuantity((Integer) quantity))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Price")
                .required(false)
                .converter(this::getDoubleValue)
                .unique(false)
                .setter((productImportData, price) -> productImportData.setPrice((Double) price))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Category")
                .required(true)
                .converter(this::getStringValue)
                .unique(false)
                .setter((productImportData, categoryName) -> productImportData.setCategoryName((String) categoryName))
                .build());

        mappings.add(HeaderMapping.<ProductImportData>builder()
                .headerName("Supplier")
                .required(true)
                .converter(this::getStringValue)
                .unique(false)
                .setter((productImportData, supplierName) -> productImportData.setSupplierName((String) supplierName))
                .build());

        return new ExcelMapper<>(ProductImportData.class, mappings);
    }
}
