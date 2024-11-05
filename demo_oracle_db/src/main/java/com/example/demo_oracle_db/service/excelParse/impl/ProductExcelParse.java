package com.example.demo_oracle_db.service.excelParse.impl;

import com.example.demo_oracle_db.service.excelParse.ExcelParse;
import com.example.demo_oracle_db.service.excelParse.map.ProductExcelConfig;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.excelParse.response.ProductImportData;
import com.example.demo_oracle_db.service.excelParse.response.SheetData;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelParse extends ExcelParse<ProductImportData> {

    private final ExcelMapper<ProductImportData> excelMapper;

    @Override
    protected SheetData<ProductImportData> parseSheet(Sheet sheet, ParseOptions options) {
        return excelMapper.mapFromExcel(sheet, options);
    }

    @Autowired
    public ProductExcelParse(ProductExcelConfig productExcelConfig) {
        this.excelMapper = productExcelConfig.productExcelMapper();
    }

}
