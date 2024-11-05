package com.example.demo_oracle_db.service.excelParse.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProductImportData {

    private Long id;
    @Length(max = 100)
    @NotBlank
    private String name;
    @NotNull
    @Range(min = 1955)
    private Long yearMaking;

    private LocalDate expireDate;
    @Range(min = 0)
    private Integer quantity;
    @Range(min = 0)
    private Double price;
    @NotBlank
    private String categoryName;
    @NotBlank
    private String supplierName;
}
