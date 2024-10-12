package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CMD_FUNCTION", schema = "DEMO")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;
    @Basic
    @Column(name = "END_POINT", nullable = true, length = 100)
    private String endPoint;

}