package com.example.demo_oracle_db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "CMD_VOUCHER", schema = "DEMO")
public class Voucher {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;
    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 255)
    private String description;
    @Basic
    @Column(name = "DISCOUNT_AMOUNT", nullable = true, precision = 2)
    private Integer discountAmount;
    @Basic
    @Column(name = "DISCOUNT_PERCENT", nullable = true, precision = 2)
    private Short discountPercent;
    @Basic
    @Column(name = "START_DATE", nullable = true)
    private Date startDate;
    @Basic
    @Column(name = "END_DATE", nullable = true)
    private Date endDate;
    @Basic
    @Column(name = "MINIMUM_ORDER_VALUE", nullable = true, precision = 2)
    private Integer minimumOrderValue;
    @Basic
    @Column(name = "MAXIMUM_DISCOUNT", nullable = true, precision = 2)
    private Integer maximumDiscount;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVoucher> productVouchers;
}
