package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RolePermissionRepository extends CrudRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {

    @Query(value = "SELECT exists(select 1 from cmd_role_permission " +
            " where ROLE_ID = ?1 and PERMISSION_ID = ?2)", nativeQuery = true)
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE from cmd_role_permission WHERE ROLE_ID = :?1 AND PERMISSION_ID = :?2", nativeQuery = true)
    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE from cmd_role_permission WHERE PERMISSION_ID = ?1 ", nativeQuery = true)
    void deleteByPermissionId(Long permissionId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "Insert Into  cmd_role_permission " +
            " (permission_id, role_id) " +
            "values (?1,?2)")
    void addPermissionRole(Long permissionId, Long roleId);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();
    @Modifying
    @Transactional
    @Query(value = "DELETE from cmd_role_permission crp " +
            " Where ROLE_ID = ?2 and EXISTS(" +
            " SELECT 1 from  cmd_permission cp " +
            " where crp.PERMISSION_ID = cp.ID and FUNCTION_ID = ?1" +
            ")",nativeQuery = true)
    void deleteByFunctionId(Long functionId, Long roleId);
}
