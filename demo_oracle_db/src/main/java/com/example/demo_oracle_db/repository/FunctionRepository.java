package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Function;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FunctionRepository extends CrudRepository<Function, Long>, JpaSpecificationExecutor<Function> {


    boolean existsByFunctionName(String functionName);

    boolean existsByFunctionNameAndIdNot(String name, Long id);

    boolean existsByFeRoute(String feRoute);
}
