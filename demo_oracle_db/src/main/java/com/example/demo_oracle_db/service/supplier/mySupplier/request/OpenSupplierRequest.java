package com.example.demo_oracle_db.service.supplier.mySupplier.request;

import com.example.demo_oracle_db.validate.PhoneConstraint;
import com.example.demo_oracle_db.validate.WebsiteConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenSupplierRequest {
    @NotBlank
    String name;
    @NotBlank
    String contact;
    @NotBlank
    String address;
    @NotBlank
    @PhoneConstraint
    String phone;
    @Email
    String email;
    @WebsiteConstraint
    String website;
}
