package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CMD_ORDER_DETAIL", schema = "DEMO")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "ORDER_ID", nullable = false, precision = 0)
    private Long orderId;
    @Basic
    @Column(name = "PRODUCT_ID", nullable = false, precision = 0)
    private Long productId;
    @Basic
    @Column(name = "QUANTITY", nullable = true, precision = 0)
    private Integer quantity;
    @Basic
    @Column(name = "UNIT_PRICE", nullable = true, precision = 2)
    private BigDecimal unitPrice;
    @Basic
    @Column(name = "DISCOUNT", nullable = true, precision = 2)
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

}
