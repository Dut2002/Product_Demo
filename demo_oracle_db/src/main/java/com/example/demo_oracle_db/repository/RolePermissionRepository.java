package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends CrudRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {
    List<RolePermission> findAllByPermissionId(Long id);
}
