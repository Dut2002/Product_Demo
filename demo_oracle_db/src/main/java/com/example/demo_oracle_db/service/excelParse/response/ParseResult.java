package com.example.demo_oracle_db.service.excelParse.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParseResult<T> {
    private boolean success;
    private List<SheetData<T>> sheets;
    private List<String> globalErrors;
    private List<T> listData;
}

