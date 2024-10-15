package com.example.demo_oracle_db.service.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddVoucherRequest {
    @NotNull(message = "Product is required")
    private Long productId;

    @NotBlank(message = "Voucher code is required")
    private String voucherCode;
}
