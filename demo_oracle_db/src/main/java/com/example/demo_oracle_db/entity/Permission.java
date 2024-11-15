package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CMD_PERMISSION", schema = "DEMO")
public class Permission {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    @Basic
    @Column(name = "BE_END_POINT")
    private String beEndPoint;
    @Basic
    @Column(name = "FUNCTION_ID")
    private Long functionId;

    @Basic
    @Column(name = "DEFAULT_PERMISSION")
    private Integer defaultPermission;

    @ManyToOne
    @JoinColumn(name = "FUNCTION_ID", insertable = false, updatable = false)
    private Function function;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RolePermission> rolePermissionList;
}
