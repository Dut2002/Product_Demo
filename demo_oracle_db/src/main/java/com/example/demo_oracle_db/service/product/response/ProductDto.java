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
    private List<VoucherDto> vouchers;


    public ProductDto(Long id, String name, Long yearMaking, LocalDate expireDate, Integer quantity, Double price, Long categoryId, String categoryName, Long supplierId, String supplierName) {
        this.id = id;
        this.name = name;
        this.yearMaking = yearMaking;
        this.expireDate = expireDate;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
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
                .map(ProductVoucher::getVoucher) // Lấy Voucher từ ProductVoucher
                .map(VoucherDto::new) // Chuyển đổi Voucher thành VoucherDto
                .collect(Collectors.toList())); // Tập hợp thành danh sách
    }
}
