package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CMD_CATEGORY", schema = "DEMO")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "NAME", nullable = false, length = 100)
    @Size(max = 100)
    private String name;
}
