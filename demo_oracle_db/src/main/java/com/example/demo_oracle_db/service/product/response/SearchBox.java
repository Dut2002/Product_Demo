package com.example.demo_oracle_db.service.product.response;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Category;
import com.example.demo_oracle_db.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
}
