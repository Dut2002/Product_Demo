package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "CMD_ORDER_DETAIL", schema = "DEMO")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;
    @Basic
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    @Basic
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Basic
    @Column(name = "UNIT_PRICE", precision = 2)
    private BigDecimal unitPrice;
    @Basic
    @Column(name = "DISCOUNT", precision = 2)
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", insertable = false, updatable = false)
    private Order order;

}
