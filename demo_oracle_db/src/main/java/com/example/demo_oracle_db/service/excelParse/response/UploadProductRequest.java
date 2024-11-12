package com.example.demo_oracle_db.service.excelParse.response;

import com.example.demo_oracle_db.util.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UploadProductRequest {

    @NotNull
    MultipartFile file;
    List<Integer> sheets;
    Integer headerRow;
    Boolean strictMapping;
    @NotNull
    Integer typeImport;

    public void setTypeImport(Integer type) {
        try {
            if (type == null) throw new Exception();
            else typeImport = Constants.TypeImport.getType(type);
        } catch (Exception e) {
            typeImport = Constants.TypeImport.ADD;
        }
    }
}
