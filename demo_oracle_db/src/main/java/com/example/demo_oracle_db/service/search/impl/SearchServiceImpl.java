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
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    EntityManager entityManager;

    @Override
    public List<SearchBox> getCategoryBox(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Category> root = query.from(Category.class);

        // Chỉ chọn các cột `id` và `name`
        query.multiselect(root.get("id"), root.get("name"));

        // Thêm điều kiện lọc nếu `name` không null hoặc không rỗng
        if (name != null && !name.isBlank()) {
            String key = "%" + name.toLowerCase().trim() + "%";
            query.where(cb.like(cb.lower(root.get("name")), key));
        }

        // Thực thi truy vấn và lấy kết quả
        List<Object[]> results = entityManager.createQuery(query).getResultList();


        return results.stream().map(SearchBox::new).toList();
    }

    @Override
    public List<SearchBox> getSupplierBox(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Supplier> root = query.from(Supplier.class);
        query.multiselect(root.get("id"), root.get("name"));
        if (name != null && !name.isBlank()) {
            String key = name.toLowerCase().trim();
            query.where(cb.like(cb.lower(root.get("name")), "%" + key + "%"));
        }
        List<Object[]> results = entityManager.createQuery(query).getResultList();
        return results.stream().map(SearchBox::new).toList();
    }

    @Override
    public List<SearchBox> getCustomerBox(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Account> root = query.from(Account.class);
        query.multiselect(root.get("id"), root.get("fullName"));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.join("accountRoles").join("role").get("name"), Constants.Role.CUSTOMER));

        // Thêm điều kiện cho `name` nếu không null hoặc không rỗng
        if (name != null && !name.isBlank()) {
            String key = "%" + name.toLowerCase().trim() + "%";
            predicates.add(cb.like(cb.lower(root.get("fullName")), key));
        }
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Object[]> results = entityManager.createQuery(query).getResultList();
        return results.stream().map(SearchBox::new).toList();
    }
}
