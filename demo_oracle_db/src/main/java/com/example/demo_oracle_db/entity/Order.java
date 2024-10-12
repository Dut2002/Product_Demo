package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CMD_ORDER", schema = "DEMO")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "CUSTOMER_ID", nullable = false, precision = 0)
    private Long customerId;
    @Basic
    @Column(name = "ORDER_DATE", nullable = true)
    private LocalDate orderDate;
    @Basic
    @Column(name = "TOTAL", nullable = true, precision = 0)
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", insertable=false, updatable=false)
    private Account customer;
}
