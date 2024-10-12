package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.entity.Category;
import com.example.demo_oracle_db.entity.Product;
import com.example.demo_oracle_db.entity.Supplier;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.product.ProductService;
import com.example.demo_oracle_db.service.product.request.ProductFilter;
import com.example.demo_oracle_db.service.product.request.ProductRequest;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("get-products")
    private ResponseEntity<?> getProducts(@RequestBody ProductFilter productFilter) {
        return ResponseEntity.ok(productService.getProducts(productFilter));
    }

    @GetMapping("get-product")
    private ResponseEntity<?> getProduct(@RequestParam Long id) throws DodException {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("add-product")
    private ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequest product) throws DodException {
        productService.addProduct(product, 1);
        return ResponseEntity.ok(new Res().resOk("Add new Product successfully"));
    }

    @PutMapping("update-product")
    private ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest product) throws DodException {
        productService.updateProduct(product, 1);
        return ResponseEntity.ok(new Res().resOk("Update Product successfully"));
    }

    @DeleteMapping("delete-product")
    private ResponseEntity<?> deleteProduct(@RequestParam Long id) throws DodException {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new Res().resOk("Delete Product successfully"));
    }

    @PostMapping("import-products")
    private ResponseEntity<?> importProducts(@RequestBody @Valid List<ProductRequest> products) throws DodException {

        return ResponseEntity.ok(productService.importProducts(products));
    }

    @GetMapping("export-products")
    private ResponseEntity<?> exportProducts(@RequestParam(required = false) String search,
                                             @RequestParam(required = false) Long yearMaking,
                                             @RequestParam(required = false) LocalDate startExpireDate,
                                             @RequestParam(required = false) LocalDate endExpireDate,
                                             @RequestParam(required = false) Long startQuality,
                                             @RequestParam(required = false) Long endQuality,
                                             @RequestParam(required = false) Double startPrice,
                                             @RequestParam(required = false) Double endPrice){
        List<Product> products = productService.exportProduct(search, yearMaking, startExpireDate, endExpireDate, startQuality, endQuality, startPrice, endPrice);
        return ResponseEntity.ok(products);
    }

    @PostMapping("get-products-procedure")
    private ResponseEntity<?> getProductsProcedure(@RequestBody ProductFilter filter){
        Page<Product> products = productService.getProductsProcedure(filter);
        return ResponseEntity.ok(products);
    }

    @GetMapping("get-category-box")
    private ResponseEntity<?> getCategoryBox(@RequestParam(required = false) String name)
    {
        List<SearchBox> categories = productService.getCategoryBox(name);
        return  ResponseEntity.ok(categories);
    }

    @GetMapping("get-supplier-box")
    private ResponseEntity<?> getSupplierBox(@RequestParam(required = false) String name)
    {
        List<SearchBox> suppliers = productService.getSupplierBox(name);
        return  ResponseEntity.ok(suppliers);
    }

    @GetMapping("get-customer-box")
    private ResponseEntity<?> getCustomerBox(@RequestParam(required = false) String name)
    {
        List<SearchBox> suppliers = productService.getCustomerBox(name);
        return  ResponseEntity.ok(suppliers);
    }
}
