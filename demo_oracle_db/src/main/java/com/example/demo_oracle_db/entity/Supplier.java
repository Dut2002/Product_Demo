package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CMD_SUPPLIER", schema = "DEMO")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "NAME", nullable = false, length = 255)
    private String name;
    @Basic
    @Column(name = "CONTACT", nullable = true, length = 255)
    private String contact;
    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 255)
    private String address;
    @Basic
    @Column(name = "PHONE", nullable = true, length = 20)
    private String phone;
    @Basic
    @Column(name = "EMAIL", nullable = true, length = 255)
    private String email;
    @Basic
    @Column(name = "WEBSITE", nullable = true, length = 255)
    private String website;
}
