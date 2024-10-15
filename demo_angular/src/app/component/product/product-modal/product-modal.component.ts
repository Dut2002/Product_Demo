import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { Product } from '../../../model/product';
import { ProductService } from '../../../service/product/product.service';
import { ApiStatus } from '../../../constant/api.const.urls';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { SearchBoxDto } from '../../../model/dto/search-box-dto';
import { NgForm, NgModel } from '@angular/forms';

@Component({
  selector: 'app-product-modal',
  templateUrl: './product-modal.component.html',
  styleUrl: './product-modal.component.scss'
})
export class ProductModalComponent implements OnInit, OnChanges {

  @Input() modalTitle: string = '';
  @Input() product: Product = {} as Product;
  @Output() successEvent = new EventEmitter<void>(); // Event khi nhấn yes
  @Output() cancelEvent = new EventEmitter<void>();

  categoryBox: SearchBoxDto[] = []
  supplierBox: SearchBoxDto[] = []
  defaultProduct: Product = {} as Product;

  @ViewChild('productForm', { static: false }) productForm!: NgForm;
  @ViewChild('category', { static: false }) categoryNgModel!: NgModel;
  @ViewChild('supplier', { static: false }) supplierNgModel!: NgModel;

  constructor(private productService: ProductService,
    private snackBarService: SnackBarService,
    private errorHandle: ErrorHandleService,
  ) {}

  ngOnInit(): void {
    this.loadCategory(null)
    this.loadSupplier(null)
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['product'])
      this.defaultProduct = { ...this.product }
  }

  loadCategory(name: string | null) {
    this.productService.getCategoryBox(name).subscribe({
      next: (response) => {
        this.categoryBox = response;
      },
      error: (err) => {
        this.errorHandle.handle(err);
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
        this.errorHandle.handle(err);
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
    if(!this.checkFormValid()) {
      this.productForm.control.markAllAsTouched();
      this.categoryNgModel.control.markAsTouched();
      this.supplierNgModel.control.markAsTouched();
      return;
    }
    if (this.product.id) {
      // Update existing product
      this.productService.updateProduct(this.product).subscribe({
        next: (response) => {
          this.onSuccess();
          this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
        },
        error: (error) => {
          this.errorHandle.handle(error)
        }
      });
    } else {
      // Add new product
      this.productService.addProduct(this.product).subscribe({
        next: (response) => {
          // Xử lý khi thêm sản phẩm thành công
          this.onSuccess();
          this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
        },
        error: (error) => {
          this.errorHandle.handle(error);
        }
      });
    }
  }
  checkFormValid() {
    return this.productForm.valid && this.categoryNgModel.valid && this.supplierNgModel.valid;
  }



  onSuccess() {
    this.successEvent.emit();
  }
  onCancel() {
    this.cancelEvent.emit();
  }

  onReset() {
    this.product = { ...this.defaultProduct }
    this.productForm.control.markAsUntouched();
    this.categoryNgModel.control.markAsUntouched();
    this.supplierNgModel.control.markAsUntouched();
  }

}
