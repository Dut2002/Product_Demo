package com.example.demo_oracle_db.service.excelParse.response;

import com.example.demo_oracle_db.util.Constants;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class ParseOptions {
    private List<Integer> sheets;
    private Integer headerRow;
    private Boolean strictMapping;
    private Integer typeImport;

    public ParseOptions(List<Integer> sheets, Integer headerRow, Boolean strictMapping, Integer typeImport) {
        this.sheets = sheets;
        this.headerRow = headerRow!=null? headerRow:1;
        this.strictMapping = strictMapping!=null? strictMapping:true;
        this.typeImport = typeImport!= null?typeImport: Constants.TypeImport.ADD;
    }



}
