package com.example.demo_oracle_db.service.search.impl;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Category;
import com.example.demo_oracle_db.entity.Supplier;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.CategoryRepository;
import com.example.demo_oracle_db.repository.SupplierRepository;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.search.SearchService;
import com.example.demo_oracle_db.util.Constants;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<SearchBox> getCategoryBox(String name) {
        List<Category> categories = categoryRepository.findAll((Specification<Category>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isBlank()) {
                String key = name.toLowerCase().trim();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + key + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return categories.stream().map(SearchBox::new).toList();
    }

    @Override
    public List<SearchBox> getSupplierBox(String name) {
        List<Supplier> suppliers = supplierRepository.findAll((Specification<Supplier>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isBlank()) {
                String key = name.toLowerCase().trim();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + key + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return suppliers.stream().map(SearchBox::new).toList();
    }

    @Override
    public List<SearchBox> getCustomerBox(String name) {
        List<Account> accounts = accountRepository.findAll((Specification<Account>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.join("accountRoles").join("role").get("name"), Constants.Role.CUSTOMER));
            if (name != null && !name.isBlank()) {
                String key = name.toLowerCase().trim();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + key + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return accounts.stream().map(SearchBox::new).toList();
    }
}
