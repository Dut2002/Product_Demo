package com.example.demo_oracle_db.service.excelParse.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetData<T> {
    private String sheetName;
    private List<T> data;
    private List<String> errors;
}

