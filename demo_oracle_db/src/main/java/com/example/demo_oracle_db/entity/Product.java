package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "CMD_PRODUCT", schema = "DEMO")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "NAME", length = 100, nullable = true)
    private String name;
    @Basic
    @Column(name = "YEAR_MAKING", nullable = true, precision = 0)
    private Long yearMaking;
    @Basic
    @Column(name = "EXPIRE_DATE", nullable = true)
    private LocalDate expireDate;
    @Basic
    @Column(name = "QUANTITY", nullable = true, precision = 0)
    private Integer quantity;
    @Basic
    @Column(name = "PRICE", nullable = true, precision = 0)
    private Double price;
    @Basic
    @Column(name = "CATEGORY_ID", nullable = false, precision = 0)
    private Long categoryId;
    @Basic
    @Column(name = "SUPPLIER_ID", nullable = false, precision = 0)
    private Long supplierId;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", insertable=false, updatable=false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID", insertable=false, updatable=false)
    private Supplier supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVoucher> productVouchers;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
}