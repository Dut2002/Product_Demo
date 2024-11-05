package com.example.demo_oracle_db.service.excelParse.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseOptions {
    private List<Integer> sheets;
    private Boolean skipUnique;
    private Integer headerRow;
    private Boolean strictMapping;

    public void setHeaderRow(Integer headerRow) {
        // Nếu headerRow được truyền vào là null, gán giá trị mặc định là 1
        this.headerRow = (headerRow != null) ? headerRow : 1;
    }

    public void setSkipUnique(Boolean skipUnique) {
        this.skipUnique = (skipUnique != null) ? skipUnique : false; // Mặc định là false
    }

    public void setStrictMapping(Boolean strictMapping) {
        this.strictMapping = (strictMapping != null) ? strictMapping : false;
    }
}
