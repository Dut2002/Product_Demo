package com.example.demo_oracle_db.service.mySupplier.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SupplierInfo {
    private Long id;
    private String name;
    private String contact;
    private String address;
    private String phone;
    private String email;
    private String website;
}
