package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends CrudRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(String name);

    boolean existsByName(@Size(max = 255) @NotBlank String name);
}
