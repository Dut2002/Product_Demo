package com.example.demo_oracle_db.service.product.response;

import com.example.demo_oracle_db.entity.Voucher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDto {
    private Long id;

    private String code;

    private String description;

    private Integer discountAmount;

    private Short discountPercent;

    private Date startDate;

    private Date endDate;

    private Integer minimumOrderValue;

    private Integer maximumDiscount;

    public VoucherDto(Voucher voucher) {
        this.id = voucher.getId(); // Gán id
        this.code = voucher.getCode(); // Gán code
        this.description = voucher.getDescription(); // Gán description
        this.discountAmount = voucher.getDiscountAmount(); // Gán discountAmount
        this.discountPercent = voucher.getDiscountPercent(); // Gán discountPercent
        this.startDate = voucher.getStartDate(); // Gán startDate
        this.endDate = voucher.getEndDate(); // Gán endDate
        this.minimumOrderValue = voucher.getMinimumOrderValue(); // Gán minimumOrderValue
        this.maximumDiscount = voucher.getMaximumDiscount(); // Gán maximumDiscount
    }
}
