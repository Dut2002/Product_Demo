package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Function;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FunctionRepository extends CrudRepository<Function, Long>, JpaSpecificationExecutor<Function> {

    @Query(value = "SELECT f.* FROM CMD_FUNCTION f JOIN CMD_ROLE_FUNCTION rf ON f.ID = rf.FUNCTION_ID " +
            " WHERE rf.ROLE_ID = :roleId AND f.END_POINT = :endPoint and rf.STATUS = 1 ", nativeQuery = true)
    Optional<Function> findByEndPointAndRole(@Param("endPoint") String endPoint, @Param("roleId") Long roleId);

    @Query(value = "SELECT f.* FROM CMD_FUNCTION f JOIN CMD_ROLE_FUNCTION rf ON f.ID = rf.FUNCTION_ID " +
            " WHERE rf.ROLE_ID = :roleId AND rf.STATUS = 1", nativeQuery = true)
    List<Function> findByRole(@Param("roleId") Long roleId);

    boolean existsByFunctionName(String functionName);

    Optional<Function> findByFunctionName(String name);

    boolean existsByFunctionNameAndIdNot(String name, Long id);

}
