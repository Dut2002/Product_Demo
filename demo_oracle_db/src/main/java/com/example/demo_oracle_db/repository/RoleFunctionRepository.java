package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.RoleFunction;
import com.example.demo_oracle_db.service.function.response.IRoleAccess;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface RoleFunctionRepository extends CrudRepository<RoleFunction, Long>, JpaSpecificationExecutor<RoleFunction> {

    @Query(value = "SELECT r.ID AS roleId, " +
            " r.NAME AS roleName, " +
            " COALESCE(rf.STATUS, 0) AS permission " +
            " FROM CMD_ROLE r " +
            " LEFT JOIN CMD_ROLE_FUNCTION rf ON rf.ROLE_ID = r.ID " +
            " AND rf.FUNCTION_ID = :id " +
            " WHERE r.NAME <> 'ROLE_SYS_ADMIN'", nativeQuery = true)
    List<IRoleAccess> getFunctionDetail(Long id);

    Optional<RoleFunction> findByRoleIdAndFunctionId(Long roleId, Long functionId);

    List<RoleFunction> findAllByFunctionId(Long functionId);
}
