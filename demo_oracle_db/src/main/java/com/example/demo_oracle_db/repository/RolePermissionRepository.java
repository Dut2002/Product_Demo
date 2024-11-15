package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolePermissionRepository extends CrudRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {


    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE from cmd_role_permission WHERE ROLE_ID = ?1 AND PERMISSION_ID = ?2", nativeQuery = true)
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

    @Query(nativeQuery = true, value = "select distinct crp.ROLE_ID from cmd_function " +
            " inner join demo.cmd_permission cp on cmd_function.ID = ?1 && cmd_function.ID = cp.FUNCTION_ID " +
            " inner join demo.cmd_role_permission crp on cp.ID = crp.PERMISSION_ID")
    List<Long> findRoleWithFuctionId(Long functionId);

    @Query(nativeQuery = true, value = "select cmd_role.ID from cmd_role " +
            " where EXISTS( " +
            " select  1 from cmd_function " +
            " inner join cmd_permission cp on cmd_function.ID = ?1 and cmd_function.ID = cp.FUNCTION_ID " +
            " inner join cmd_role_permission crp on cp.ID = crp.PERMISSION_ID and ROLE_ID = cmd_role.ID "+
            " ) AND NOT EXISTS( " +
            " select 1 from cmd_role_permission where PERMISSION_ID = ?2 and ROLE_ID = cmd_role.ID " +
            ")")
    List<Long> findRoleWithFuctionIdAndPermissionIdNot(Long functionId, Long permissionId);
}
