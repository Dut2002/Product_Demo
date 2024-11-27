package com.example.demo_oracle_db.service.product.response;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Category;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.entity.Supplier;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBox {
    private Long id;
    private String name;


    public SearchBox(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }
    public SearchBox(Supplier supplier){
        this.id = supplier.getId();
        this.name = supplier.getName();
    }

    public SearchBox(Account customer){
        this.id = customer.getId();
        this.name = customer.getFullName();
    }

    public SearchBox(Role role){
        this.id = role.getId();
        this.name = role.getName().replace("ROLE_","").replace("_", " ");
    }

    public SearchBox(Object[] o) {
        this.id = ((Long) o[0]);
        this.name = o[1].toString();
    }

    public SearchBox(Long id, String name) {
        this.id = id;
        this.name = name.replace("ROLE_","").replace("_", " ");
    }
}
