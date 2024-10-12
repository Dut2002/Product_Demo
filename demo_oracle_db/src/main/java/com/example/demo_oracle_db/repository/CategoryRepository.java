package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Category;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepository extends CrudRepository<Category, Long>, JpaSpecificationExecutor<Category> {

}
