import { Component, OnInit, ViewChild } from '@angular/core';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { ProductDto } from '../../../model/dto/product-dto';
import { ProductFilter } from '../../../model/dto/product-filter';
import { Product } from '../../../model/product';

import { HttpParams } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { CommonService } from '../../../service/common/common.service';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { ProductImportComponent } from '../product-import/product-import.component';
import { ProductModalComponent } from '../product-modal/product-modal.component';
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})


export class ProductComponent implements OnInit {

  @ViewChild('productModal') productModal!: ProductModalComponent;
  @ViewChild('productConfirm') productConfirm!: ConfirmModalComponent;
  @ViewChild('productImport') productImport!: ProductImportComponent;

  products: ProductDto[] = [];
  currentProduct: Product = {} as Product;

  productFilter: ProductFilter = new ProductFilter;

  totalPages: number = 0;

  permission = RouterUrl;


  constructor(public common: CommonService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.common.setFunctionName(this.route)
    this.loadProducts();
  }

  loadProducts(): void {
    const endpoint = this.common.getPermission(PermissionName.ProductManagement.VIEW_PRODUCTS)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, this.productFilter).subscribe({
      next: (response) => {
        this.products = response.content; // Gán dữ liệu vào products
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        this.common.errorHandle.handle(error)
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
    this.productModal.modalTitle = 'Add New Product';
    this.productModal.openModal(true);
  }

  openUpdateModal(product: ProductDto) {
    this.currentProduct = { ...product };
    this.productModal.modalTitle = 'Update Product';
    this.productModal.openModal(true);
  }

  saveSuccess() {
    this.loadProducts();
  }

  confirmDelete(product: ProductDto) {
    this.currentProduct = { ...product };
    this.productConfirm.showConfirmation = true;
  }

  deleteProduct() {
    const endpoint = this.common.getPermission(PermissionName.ProductManagement.DELETE_PRODUCT)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams();
    params = params.set("id", this.currentProduct.id);
    // Add new product
    this.common.base.delete(endpoint, params)
      .pipe(finalize(() => {
        this.productConfirm.isLoading = false;
      }))
      .subscribe({
        next: (response) => {
          // Xử lý khi thêm sản phẩm thành công
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          this.loadProducts();
        },
        error: (error) => {
          // Xử lý lỗi khi thêm sản phẩm
          this.common.errorHandle.handle(error);
        }
      });
  }

  importProducts() {
    // Implement import functionality
    this.productImport.showModal = true;
  }

  exportProducts() {
    const endpoint = this.common.getPermission(PermissionName.ProductManagement.EXPORT_PRODUCT)
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    let params = new HttpParams()
    params = params.set('option', 'xlsx');
    // Implement export functionality
    this.common.base.export(endpoint, params).subscribe({
      next: (response) => {

        const blob = new Blob([response.body!], { type: response.body?.type || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        // Tạo URL cho blob và tải xuống
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const currentDate = new Date();
        const formattedDate = currentDate.toISOString().replace(/:/g, '-');
        a.download = 'Product Report ' + formattedDate + '.xlsx' // Sử dụng tên file từ header hoặc tên mặc định
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: err => {
        this.common.errorHandle.handle(err);
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
