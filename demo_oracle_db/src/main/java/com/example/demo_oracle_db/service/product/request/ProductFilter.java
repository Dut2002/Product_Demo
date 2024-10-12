package com.example.demo_oracle_db.service.product.request;

import com.example.demo_oracle_db.validate.CustomDateConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilter {
    private String name;
    private Integer yearMaking;
    @CustomDateConstraint(pattern = "yyyy-MM-dd", message = "Start date must be format yyyy-MM-dd")
    private String startExpireDate;
    @CustomDateConstraint(pattern = "yyyy-MM-dd", message = "End date must be format yyyy-MM-dd")
    private String endExpireDate;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Double minPrice;
    private Double maxPrice;
    private Integer pageNum;
    private Integer sizePage;
    private String orderCol;
    private Boolean sortDesc;

    private Long categoryId;
    private Long supplierId;
    private Long customerId;
    private String voucherCode;
}
