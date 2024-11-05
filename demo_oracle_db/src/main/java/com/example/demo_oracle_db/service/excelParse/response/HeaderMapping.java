package com.example.demo_oracle_db.service.excelParse.response;

import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Data
@Builder
public class HeaderMapping<T> {
    private String headerName;           // Tên cột trong Excel
    private BiConsumer<T, Object> setter;  // Setter method của field trong class
    private Function<Cell, Object> converter; // Hàm chuyển đổi giá trị từ Excel cell
    private Function<Object, Boolean> exists;     // Hàm check tồn tại
    private boolean required;
    private boolean unique;

}
