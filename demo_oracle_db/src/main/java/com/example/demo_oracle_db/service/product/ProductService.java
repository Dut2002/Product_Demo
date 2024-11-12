package com.example.demo_oracle_db.service.product;

import com.example.demo_oracle_db.entity.Product;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.excelParse.response.ParseResult;
import com.example.demo_oracle_db.service.excelParse.response.ProductImportData;
import com.example.demo_oracle_db.service.product.request.AddVoucherRequest;
import com.example.demo_oracle_db.service.product.request.ProductFilter;
import com.example.demo_oracle_db.service.product.request.ProductRequest;
import com.example.demo_oracle_db.service.product.response.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ProductService {
    Page<ProductDto> getProducts(ProductFilter productFilter);

    void addProduct(ProductRequest product) throws DodException;

    void updateProduct(ProductRequest product, int option) throws DodException;

    void deleteProduct(Long id) throws DodException;

    ParseResult<ProductImportData> importProducts(ParseResult<ProductImportData> products);

    Page<Product> getProductsProcedure(ProductFilter filter);


    void addVoucherToProduct(AddVoucherRequest request) throws DodException;

    void deleteVoucherFromProduct(Long productId, Long voucherId) throws DodException;

    ByteArrayInputStream productListReport() throws IOException;

    ParseResult<ProductImportData> parseProduct(MultipartFile file, ParseOptions options);
}
