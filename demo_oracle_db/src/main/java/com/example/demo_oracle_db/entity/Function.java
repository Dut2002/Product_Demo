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
    private String name;
    @Basic
    @Column(name = "FE_ROUTE")
    private String feRoute;
    @Basic
    @Column(name = "PRIORITY", nullable = false, precision = 0)
    private Integer priority;
    @JsonIgnore
    @OneToMany(mappedBy = "function", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Permission> permissions;

    @JsonIgnore
    @OneToMany(mappedBy = "function", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<FunctionRole> functionRoleList;
}
