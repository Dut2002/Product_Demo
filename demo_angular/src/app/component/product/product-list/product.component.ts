import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../service/product/product.service';
import { ProductFilter } from '../../../model/dto/product-filter';
import { ApiStatus } from '../../../constant/api.const.urls';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { AuthService } from '../../../service/auth/auth.service';
import { RouterUrl } from '../../../constant/app.const.router';
import { ProductDto } from '../../../model/dto/product-dto';
import { Product } from '../../../model/product';

import { HttpResponse } from '@angular/common/http';
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})


export class ProductComponent implements OnInit {
  showConfirmation = false;
  products: ProductDto[] = [];
  currentProduct: Product = {} as Product;

  productFilter: ProductFilter = new ProductFilter;

  showModal = false;
  modalTitle = '';
  totalPages: number = 0;

  permission = RouterUrl;

  constructor(private productService: ProductService,
    private snackBarService: SnackBarService,
    private errorHandle: ErrorHandleService,
    public authService: AuthService
  ) { }

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts(this.productFilter).subscribe({
      next: (response) => {
        this.products = response.content; // Gán dữ liệu vào products
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        this.errorHandle.handle(error)
      },
      complete: () => {
        console.log('Get all products request completed');
      }
    });
  }

  onPageChange(page: number) {
    this.productFilter.pageNum = page
    this.loadProducts();
  }

  applyFilter(productFilter: ProductFilter): void {
    this.productFilter = productFilter;
    this.loadProducts();
  }

  resetFilter(): void {
    this.productFilter = new ProductFilter;
    this.loadProducts();
  }

  openAddModal() {
    this.currentProduct = {} as Product;
    this.modalTitle = 'Add New Product';
    this.showModal = true;
  }

  openUpdateModal(product: ProductDto) {
    this.currentProduct = { ...product };
    this.modalTitle = 'Update Product';
    this.showModal = true;
  }

  saveSuccess() {
    this.loadProducts();
    this.closeModal();
  }

  closeModal() {
    this.showModal = false;
  }

  confirmDelete(product: ProductDto) {
    this.currentProduct = { ...product };
    this.showConfirmation = true;
  }

  deleteProduct() {
    // Add new product
    this.productService.deleteProduct(this.currentProduct.id).subscribe({
      next: (response) => {
        // Xử lý khi thêm sản phẩm thành công
        this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
        console.log('Product deleteing successfully:', response);
        this.loadProducts();
      },
      error: (error) => {
        // Xử lý lỗi khi thêm sản phẩm
        this.errorHandle.handle(error);
      },
      complete: () => {
        this.showConfirmation = false;
      }
    });
  }

  cancleConfirm() {
    this.showConfirmation = false;
  }

  importProducts() {
    // Implement import functionality
    console.log('Import functionality to be implemented');
    this.snackBarService.show(null, 'There is a message', ApiStatus.NORMAL, 1000000000)
  }

  exportProducts() {
    // Implement export functionality
    this.productService.exportProduct('xlsx').subscribe({
      next: (response:HttpResponse<Blob>) => {
        const filename = response.headers.get('File-Name');
        console.log(response.headers.keys()); // In ra tất cả các header

        alert(filename) // Lấy tên file từ header
        const blob = new Blob([response.body!], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })

        // Tạo URL cho blob và tải xuống
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename || 'default-filename'; // Sử dụng tên file từ header hoặc tên mặc định
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: err => {
        this.errorHandle.handle(err);
      }
    })
  }

  sortTable(column: string) {
    this.productFilter.pageNum = 1;
    if (this.productFilter.orderCol === column) {
      this.productFilter.sortDesc = !this.productFilter.sortDesc;
    } else {
      this.productFilter.orderCol = column;
      this.productFilter.sortDesc = false;
    }
    this.loadProducts();
  }
}
