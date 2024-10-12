package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CMD_ROLE_FUNCTION", schema = "DEMO")
public class RoleFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "ROLE_ID", nullable = false, precision = 0)
    private Long roleId;
    @Basic
    @Column(name = "FUNCTION_ID", nullable = false, precision = 0)
    private Long functionId;
    @Basic
    @Column(name = "STATUS", nullable = false, precision = 0)
    private Integer status;

    public RoleFunction(Long role, Long function){
        this.roleId = role;
        this.functionId = function;
    }
}