package com.example.demo_oracle_db.service.product.response;

import com.example.demo_oracle_db.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private Long yearMaking;

    private LocalDate expireDate;

    private Integer quantity;

    private Double price;

    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private List<Voucher> vouchers;

    public static Page<ProductDto> convertToProductDtoPage(Page<Product> productPage) {
        List<ProductDto> productDtoList = productPage.getContent().stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtoList, productPage.getPageable(), productPage.getTotalElements());
    }

    public ProductDto(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setYearMaking(product.getYearMaking());
        this.setExpireDate(product.getExpireDate());
        this.setQuantity(product.getQuantity());
        this.setPrice(product.getPrice());

        // Assuming you have methods to get category and supplier details
        if (product.getCategory() != null) {
            this.setCategoryId(product.getCategory().getId());
            this.setCategoryName(product.getCategory().getName());
        }

        if (product.getSupplier() != null) {
            this.setSupplierId(product.getSupplier().getId());
            this.setSupplierName(product.getSupplier().getName());
        }

        // Assuming product has a method to get vouchers
        this.setVouchers(product.getProductVouchers().stream()
                .map(ProductVoucher::getVoucher)
                .collect(Collectors.toList()));
    }
}
