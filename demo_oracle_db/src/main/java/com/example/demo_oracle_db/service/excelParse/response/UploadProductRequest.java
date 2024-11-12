package com.example.demo_oracle_db.service.excelParse.response;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
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

    public void setTypeImport(Integer type) throws DodException {
        try {
            if (type == null) throw new Exception();
            else typeImport = Constants.TypeImport.getType(type);
        } catch (Exception e) {
            throw new DodException(MessageCode.TYPE_IMPORT_INVALID);
        }
    }
}
