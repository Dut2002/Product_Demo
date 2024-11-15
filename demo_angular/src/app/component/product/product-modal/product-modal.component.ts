import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { Product } from '../../../model/product';
import { CommonService } from '../../../service/common/common.service';
import { ProductService } from '../../../service/product/product.service';

@Component({
  selector: 'app-product-modal',
  templateUrl: './product-modal.component.html',
  styleUrl: './product-modal.component.scss'
})
export class ProductModalComponent implements OnChanges {

  @Input() common!: CommonService;

  modalTitle: string = '';
  @Input() product: Product = {} as Product;
  @Output() successEvent = new EventEmitter<void>();
  showModal = false;
  isLoading: boolean = false;

  categoryBox: SearchBoxDto[] = []
  supplierBox: SearchBoxDto[] = []
  defaultProduct: Product = {} as Product;

  @ViewChild('productForm', { static: false }) productForm!: NgForm;
  @ViewChild('category', { static: false }) categoryNgModel!: NgModel;
  @ViewChild('supplier', { static: false }) supplierNgModel!: NgModel;

  constructor(private productService: ProductService
  ) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['product']) {
      this.loadCategory(null)
      this.loadSupplier(null)
      this.defaultProduct = { ...this.product }
    }
  }

  openModal(show: boolean) {
    this.showModal = show;
  }

  loadCategory(name: string | null) {
    this.productService.getCategoryBox(name).subscribe({
      next: (response) => {
        this.categoryBox = response;
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    }
    )
  }

  loadSupplier(name: string | null) {
    this.productService.getSupplierBox(name).subscribe({
      next: (response) => {
        this.supplierBox = response;
      },
      error: (err) => {
        this.common.errorHandle.handle(err);
      }
    }
    )
  }

  onCategorySelected(id: number | null) {
    if (id) this.product.categoryId = id;
  }

  onSupplierSelected(id: number | null) {
    if (id) this.product.supplierId = id;
  }

  saveProduct() {
    if (!this.checkFormValid()) {
      this.productForm.control.markAllAsTouched();
      this.categoryNgModel.control.markAsTouched();
      this.supplierNgModel.control.markAsTouched();
      return;
    }
    this.isLoading = true;
    if (this.product.id) {
      const endpoint = this.common.getPermission(PermissionName.ProductManagement.UPDATE_PRODUCT)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      this.common.base.put(endpoint, this.product)
        .pipe(finalize(() => {
          this.isLoading = false;
        }))
        .subscribe({
          next: (response) => {
            this.successEvent.emit();
            this.showModal = false;
            this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
          },
          error: (error) => {
            this.common.errorHandle.handle(error)
          }
        });
    } else {
      const endpoint = this.common.getPermission(PermissionName.ProductManagement.ADD_PRODUCT)
      if (!endpoint) {
        this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      } this.common.base.post(endpoint, this.product).subscribe({
        next: (response) => {
          // Xử lý khi thêm sản phẩm thành công
          this.successEvent.emit();
          this.showModal = false;
          this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000);
        },
        error: (error) => {
          this.common.errorHandle.handle(error);
        }
      });
    }
  }

  checkFormValid() {
    return this.productForm.valid && this.categoryNgModel.valid && this.supplierNgModel.valid;
  }

  onReset() {
    this.product = { ...this.defaultProduct }
    this.productForm.control.markAsUntouched();
    this.categoryNgModel.control.markAsUntouched();
    this.supplierNgModel.control.markAsUntouched();
  }

}
