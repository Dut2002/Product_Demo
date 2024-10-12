package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {
    
}
