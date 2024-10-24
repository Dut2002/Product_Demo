package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("get-category-box")
    private ResponseEntity<?> getCategoryBox(@RequestParam(required = false) String name)
    {
        List<SearchBox> categories = searchService.getCategoryBox(name);
        return  ResponseEntity.ok(categories);
    }

    @GetMapping("get-supplier-box")
    private ResponseEntity<?> getSupplierBox(@RequestParam(required = false) String name)
    {
        List<SearchBox> suppliers = searchService.getSupplierBox(name);
        return  ResponseEntity.ok(suppliers);
    }

    @GetMapping("get-customer-box")
    private ResponseEntity<?> getCustomerBox(@RequestParam(required = false) String name)
    {
        List<SearchBox> suppliers = searchService.getCustomerBox(name);
        return  ResponseEntity.ok(suppliers);
    }
}
