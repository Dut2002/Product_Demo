package com.example.demo_oracle_db.service.excelParse.response;

import com.example.demo_oracle_db.util.Constants;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParseResult<T> {
    private boolean success;
    private Integer typeImport;
    private List<SheetData<T>> sheets;
    private List<String> globalErrors;
    private List<T> listData;

    public void setTypeImport(Integer type) {
        try {
            if (type == null) throw new Exception();
            else typeImport = Constants.TypeImport.getType(type);
        } catch (Exception e) {
            typeImport = Constants.TypeImport.ADD;
        }
    }

}

