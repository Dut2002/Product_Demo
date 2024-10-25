package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RolePermissionRepository extends CrudRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {

    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
