package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CMD_FUNCTION_ROLE", schema = "DEMO")
public class FunctionRole {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "FUNCTION_ID")
    private Long functionId;
    @Basic
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;


    @ManyToOne
    @JoinColumn(name = "FUNCTION_ID", insertable = false, updatable = false)
    private Function function;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    private Role role;
}
