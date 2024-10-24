package com.example.demo_oracle_db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CMD_FUNCTION", schema = "DEMO")
public class Function {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "FUNCTION_NAME", nullable = false, unique = true)
    private String functionName;
    @Basic
    @Column(name = "FE_ROUTE")
    private String feRoute;

    @JsonIgnore
    @OneToMany(mappedBy = "function", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permission> permissions;
}
