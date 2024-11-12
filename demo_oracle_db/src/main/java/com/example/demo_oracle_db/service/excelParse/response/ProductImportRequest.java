package com.example.demo_oracle_db.service.excelParse.response;

import com.example.demo_oracle_db.util.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductImportRequest {
    @NotNull
    Constants.TypeImport typeImport;
    @NotNull
    List<ProductImportData> products;
}
