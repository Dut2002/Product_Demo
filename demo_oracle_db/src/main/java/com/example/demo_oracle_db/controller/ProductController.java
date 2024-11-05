package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.entity.Product;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.excelParse.ExcelParse;
import com.example.demo_oracle_db.service.excelParse.response.ParseOptions;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.product.ProductService;
import com.example.demo_oracle_db.service.product.request.AddVoucherRequest;
import com.example.demo_oracle_db.service.product.request.ProductFilter;
import com.example.demo_oracle_db.service.product.request.ProductRequest;
import com.example.demo_oracle_db.util.FileType;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("view-products")
    private ResponseEntity<?> getProducts(@RequestBody ProductFilter productFilter) {
        return ResponseEntity.ok(productService.getProducts(productFilter));
    }

    @PostMapping("add-product")
    private ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequest product) throws DodException {
        productService.addProduct(product);
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

    @PostMapping("upload-product")
            public ResponseEntity<?> uploadExcel(@RequestPart("file") MultipartFile file,
                                                 @ModelAttribute @Valid ParseOptions options) {

        return ResponseEntity.ok(productService.parseProduct(file, options));
    }

    @PostMapping("get-products-procedure")
    private ResponseEntity<?> getProductsProcedure(@RequestBody ProductFilter filter) {
        Page<Product> products = productService.getProductsProcedure(filter);
        return ResponseEntity.ok(products);
    }

    @PostMapping("add-voucher-product")
    private ResponseEntity<?> addVoucherToProduct(@RequestBody @Valid AddVoucherRequest request) throws DodException {
        productService.addVoucherToProduct(request);
        return ResponseEntity.ok(new Res().resOk("Add voucher to product success"));
    }

    @DeleteMapping("delete-voucher-product")
    private ResponseEntity<?> deleteVoucherFromProduct(@RequestParam Long productId,@RequestParam Long voucherId) throws DodException {
        productService.deleteVoucherFromProduct(productId, voucherId);
        return ResponseEntity.ok(new Res().resOk("Delete voucher from product success"));
    }

    @GetMapping("export-products")
    private ResponseEntity<InputStreamResource> exportProduct(@RequestParam String option) throws DodException, IOException {
        FileType fileType;
        try {
            fileType = FileType.fromOption(option);
        } catch (IllegalArgumentException e) {
            // Xử lý khi options không hợp lệ
            throw new DodException(MessageCode.ILLEGAL_EXTENSION, option);
        }
        MediaType mediaType = switch (fileType) {
            case XLSX -> MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case CSV -> MediaType.TEXT_PLAIN;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        String fileName = "Product Report " +
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) +
                "." +
                fileType.getExtension();

        String contentDisposition = "attachment; filename=\"" + fileName + "\"";

        ByteArrayInputStream data = productService.productListReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", fileName);
        headers.add("Content-Disposition", contentDisposition);
        return ResponseEntity.ok().headers(headers).contentType(mediaType).body(new InputStreamResource(data));
    }
}
