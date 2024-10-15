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
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
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

  authority = RouterUrl;

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
    this.currentProduct = {... product};
    this.modalTitle = 'Update Product';
    this.showModal = true;
  }

  saveSuccess(){
    this.loadProducts();
    this.closeModal();
  }

  closeModal() {
    this.showModal = false;
  }

  confirmDelete(product: ProductDto) {
    this.currentProduct = {...product};
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

  cancleConfirm(){
    this.showConfirmation = false;
  }

  importProducts() {
    // Implement import functionality
    console.log('Import functionality to be implemented');
    this.snackBarService.show(null, 'There is a message',ApiStatus.NORMAL, 1000000000)
  }

  exportProducts() {
    // Implement export functionality
    console.log('Export functionality to be implemented');
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
