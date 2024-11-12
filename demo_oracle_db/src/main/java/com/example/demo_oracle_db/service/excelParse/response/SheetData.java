package com.example.demo_oracle_db.service.excelParse.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetData<T> {
    private String sheetName;
    @JsonIgnore
    private List<T> data;
    private List<String> errors;
}

