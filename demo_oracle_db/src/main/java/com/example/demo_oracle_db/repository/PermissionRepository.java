package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.service.role.response.IPermissionDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PermissionRepository extends CrudRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    List<Permission> findAllByFunctionId(Long functionId);

    boolean existsByName(String name);

    boolean existsByBeEndPoint(String beEndPoint);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByBeEndPointAndIdNot(String beEndPoint, Long id);

    @Query(value = "select p.ID as id, " +
            " p.NAME as name, " +
            " p.FUNCTION_ID as functionId " +
            " from CMD_PERMISSION p" +
            " LEFT JOIN CMD_ROLE_PERMISSION pr ON p.ID = pr.PERMISSION_ID and pr.ROLE_ID = :roleId" +
            " LEFT JOIN CMD_ROLE r on r.ID = pr.ROLE_ID ",nativeQuery = true)
    List<IPermissionDto> findPermissionsByRoleId(Long roleId);

    @Query(value = "select distinct p.*  " +
            " from CMD_PERMISSION p " +
            " INNER JOIN CMD_ROLE_PERMISSION pr ON p.ID = pr.PERMISSION_ID " +
            " where pr.ROLE_ID IN :roleIds", nativeQuery = true)
    List<Permission> findPermissionsByRole(List<Long> roleIds);


    @Query(value = "select distinct p.* from CMD_PERMISSION p " +
            "INNER JOIN DEMO.CMD_ROLE_PERMISSION CRP on p.ID = CRP.PERMISSION_ID " +
            "WHERE :endPoint = p.BE_END_POINT AND CRP.ROLE_ID IN :roleIds",
            nativeQuery = true)
    Optional<Permission> findByPermissionAccess(String endPoint, List<Long> roleIds);
}
