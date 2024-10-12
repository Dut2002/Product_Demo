package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(@NotBlank(message = "Username is required") String username);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Size(max = 100) @Email(message = "Email format invalid") String email);
}
