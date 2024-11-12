package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Permission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PermissionRepository extends CrudRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    List<Permission> findAllByFunctionId(Long functionId);

    boolean existsByBeEndPoint(String beEndPoint);

    @Query(value = "select distinct p.*  " +
            " from CMD_PERMISSION p " +
            " INNER JOIN CMD_ROLE_PERMISSION pr ON p.ID = pr.PERMISSION_ID " +
            " where pr.ROLE_ID IN :roleIds", nativeQuery = true)
    List<Permission> findPermissionsByRole(List<Long> roleIds);


    @Query(value = "SELECT DISTINCT p.* FROM CMD_PERMISSION p " +
            "WHERE p.BE_END_POINT = ?1 AND " +
            "EXISTS (SELECT 1 FROM DEMO.CMD_ROLE_PERMISSION CRP " +
            "WHERE CRP.PERMISSION_ID = p.ID AND CRP.ROLE_ID IN ?2 )",
            nativeQuery = true)
    Optional<Permission> findByPermissionAccess(String endPoint,
                                                List<Long> roleIds);


    boolean existsByNameAndFunctionIdAndIdNot(String name, Long functionId, Long id);

    boolean existsByBeEndPointAndFunctionIdAndIdNot(String beEndPoint, Long functionId, Long id);


    boolean existsByNameAndFunctionId(String name, Long functionId);

    boolean existsByBeEndPointAndFunctionId(String beEndPoint, Long functionId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CMD_PERMISSION (NAME, BE_END_POINT, FUNCTION_ID ,default_permission)" +
            "values (?1,?2,?3,?4)", nativeQuery = true)
    void addPermission(String name, String beEndPoint, Long functionId, Integer required);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();
    @Modifying
    @Transactional
    @Query(value = "UPDATE CMD_PERMISSION p" +
            " SET p.NAME = ?2, p.BE_END_POINT = ?3, p.default_permission = ?4" +
            " WHERE p.id = ?1 ", nativeQuery = true)
    void updatePermission(Long id, String name, String beEndPoint, Integer required);

    @Modifying
    @Transactional
    @Query(value = "delete from cmd_permission where FUNCTION_ID = ?1", nativeQuery = true)
    void deleteByFunctionId(Long id);

    @Query(value = "select ID from cmd_permission where FUNCTION_ID = ?1", nativeQuery = true)
    List<Long> getIdByFunctionId(Long id);
    boolean existsByIdNotAndFunctionIdAndDefaultPermission(Long permissionId, Long functionId, int i);
}
