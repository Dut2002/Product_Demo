package com.example.demo_oracle_db.service.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ExportProductRequest {
    @NotNull
    ProductFilter filter;
    @NotBlank
    String option;
}
