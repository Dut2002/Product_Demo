package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends CrudRepository<Role, Long>, JpaSpecificationExecutor<Role> {


    boolean existsByName(@Size(max = 255) @NotBlank String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query(value = "select c.* from CMD_ROLE c" +
            " INNER JOIN CMD_ACCOUNT_ROLE ac on ac.ROLE_ID = c.ID " +
            " WHERE ac.ACCOUNT_ID = :accountId",nativeQuery = true)
    List<Role> findByAccount(Long accountId);

    @Query(value = "select ID from cmd_role where NAME = ?1", nativeQuery = true)
    Optional<Long> findIdByName(String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO cmd_role (NAME, priority) values (?1, ?2)")
    void addRole(String name, Integer priority);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE cmd_role SET NAME = ?2 WHERE ID = ?1 ")
    void updateRole(Long id, String name);

    @Query(nativeQuery = true, value = "select NAME from cmd_role WHERE ID = ?1")
    Optional<String> findNameById(Long roleId);

    @Query(value = "SELECT priority from cmd_role where ID = ?1", nativeQuery = true)
    Optional<Integer> findPriorityById(Long id);
}
