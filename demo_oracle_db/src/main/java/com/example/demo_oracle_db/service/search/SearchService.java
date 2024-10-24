package com.example.demo_oracle_db.service.search;

import com.example.demo_oracle_db.service.product.response.SearchBox;

import java.util.List;

public interface SearchService {

    List<SearchBox> getCategoryBox(String name);

    List<SearchBox> getSupplierBox(String name);

    List<SearchBox> getCustomerBox(String name);
}
