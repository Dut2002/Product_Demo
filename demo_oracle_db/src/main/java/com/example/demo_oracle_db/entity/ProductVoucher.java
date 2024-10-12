package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "CMD_PRODUCT_VOUCHER", schema = "DEMO")
public class ProductVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "PRODUCT_ID", nullable = false, precision = 0)
    private Long productId;
    @Basic
    @Column(name = "VOUCHER_ID", nullable = false, precision = 0)
    private Long voucherId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", insertable = false, updatable = false, referencedColumnName = "id")
    private Product product;

    // Many ProductVouchers belong to one Voucher
    @ManyToOne
    @JoinColumn(name = "VOUCHER_ID", insertable = false, updatable = false, referencedColumnName = "id")
    private Voucher voucher;

}