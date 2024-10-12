package com.example.demo_oracle_db.service.product.request;

import com.example.demo_oracle_db.validate.CustomDateConstraint;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
@Setter
public class ProductRequest {

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(Map<String,String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be below 100 characters")
    private String name;

    @NotNull(message = "Year making is required")
    private Long yearMaking;

    @CustomDateConstraint(pattern = "yyyy-MM-dd", message = "Expiration date must be in the format yyyy-MM-dd")
    private String expireDate;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    @NotNull(message = "Category is required")
    private Long categoryId;
    @NotNull(message = "Supplier is required")
    private Long supplierId;

    private String status; //Success/Failed
    private Object errorMessage; //Error description
}
