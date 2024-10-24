package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CMD_ROLE_PERMISSION", schema = "DEMO")
public class RolePermission {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
    @Basic
    @Column(name = "PERMISSION_ID", nullable = false)
    private Long permissionId;

    @ManyToOne
    @JoinColumn(name = "PERMISSION_ID", insertable = false, updatable = false)
    private Permission permission;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    private Role role;
}
