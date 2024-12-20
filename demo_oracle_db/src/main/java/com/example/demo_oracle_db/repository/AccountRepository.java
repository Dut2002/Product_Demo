package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Account;
import jakarta.validation.constraints.Email;
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
public interface AccountRepository extends CrudRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(@NotBlank(message = "Username is required") String username);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Size(max = 100) @Email(message = "Email format invalid") String email);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_account " +
            " SET access_token = ?1, refresh_token = ?2 " +
            " WHere ID = ?3", nativeQuery = true)
    void updateToken(String accessToken, String refreshToken, Long id);

    @Modifying
    @Query(value = "INSERT INTO cmd_account" +
            " (USERNAME, PASSWORD, EMAIL, FULL_NAME, status) " +
            " values (?1,?2,?3,?4, ?5)",nativeQuery = true)
    void addAccount(String username, String password, String email, String fullName, String status);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_account" +
            " SET access_token = null, refresh_token = null "+
            " WHERE  USERNAME = ?1 ",nativeQuery = true)
    void logout(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_account " +
            " SET USERNAME = ?2, EMAIL = ?3, FULL_NAME = ?4 " +
            " WHERE ID = ?1",nativeQuery = true)
    void updateAccount(Long id, String username, String email, String fullName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_account " +
            " SET STATUS = ?4 " +
            " WHERE ID = ?1",nativeQuery = true)
    void changeStatus(Long id, String status);

    @Query(value = "select ID from cmd_account " +
            " WHERE USERNAME = ?1",nativeQuery = true)
    Optional<Long> getIdByUserName(String username);
    @Query(value = "select FULL_NAME from cmd_account " +
            " WHERE ID = ?1",nativeQuery = true)
    Optional<String> getFullNameById(Long accountId);

    @Modifying
    @Transactional
    @Query(value = "Update cmd_account " +
            " set password = ?2, status = ?3 where id = ?1 ", nativeQuery = true)
    void setPassword(Long id, String encode, String status);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "Update cmd_account set status = ?2 where ID = ?1")
    void setStatus(Long id, String name);

    @Query(value = "select username from cmd_role " +
            " inner join cmd_account_role on NAME = ?1 && cmd_role.ID = ROLE_ID" +
            " inner join demo.cmd_account on cmd_account_role.ACCOUNT_ID = cmd_account.ID", nativeQuery = true)
    List<String> getUsernameByRoleName(String roleName);

    @Query(nativeQuery = true, value = "select cmd_account.id from cmd_role " +
            " inner join cmd_account_role on NAME = ?1 and cmd_role.ID = cmd_account_role.ROLE_ID " +
            " inner join cmd_account on cmd_account_role.ACCOUNT_ID = cmd_account.ID")
    List<Long> findByRoleName(String roleName);
}

