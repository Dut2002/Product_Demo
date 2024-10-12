package com.example.demo_oracle_db.service.product;

import com.example.demo_oracle_db.entity.Category;
import com.example.demo_oracle_db.entity.Product;
import com.example.demo_oracle_db.entity.Supplier;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.product.request.ProductFilter;
import com.example.demo_oracle_db.service.product.request.ProductRequest;
import com.example.demo_oracle_db.service.product.response.ProductDto;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    Page<ProductDto> getProducts(ProductFilter productFilter);

    Product getProduct(Long id) throws DodException;

    ProductRequest addProduct(ProductRequest product,  int option) throws DodException;

    ProductRequest updateProduct(ProductRequest product,  int option) throws DodException;

    void deleteProduct(Long id) throws DodException;

    List<ProductRequest> importProducts(List<ProductRequest> products) throws DodException;

    List<Product> exportProduct(String search, Long yearMaking, LocalDate startExpireDate, LocalDate endExpireDate, Long startQuality, Long endQuality, Double startPrice, Double endPrice);

    Page<Product> getProductsProcedure(ProductFilter filter);

    List<SearchBox> getCategoryBox(String name);

    List<SearchBox> getSupplierBox(String name);

    List<SearchBox> getCustomerBox(String name);
}
