package com.example.demo_oracle_db.service.mySupplier.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierFilter {
    private String name;
    private String contact;
    private String address;
    private String phone;
    private String email;
    private String website;

    private Integer pageNum;
    private Integer pageSize;
}
