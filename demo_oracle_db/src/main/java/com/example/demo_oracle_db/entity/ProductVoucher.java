package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "CMD_PRODUCT_VOUCHER", schema = "DEMO")
public class ProductVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    @Basic
    @Column(name = "VOUCHER_ID", nullable = false)
    private Long voucherId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "VOUCHER_ID", insertable = false, updatable = false)
    private Voucher voucher;

}