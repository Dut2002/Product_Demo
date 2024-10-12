import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { Product } from '../../../model/product';
import { ProductComponent } from '../product-list/product.component';
import { ProductService } from '../../../service/product/product.service';
import { ApiStatus } from '../../../constant/api.const.urls';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';

@Component({
  selector: 'app-product-modal',
  templateUrl: './product-modal.component.html',
  styleUrl: './product-modal.component.scss'
})
export class ProductModalComponent implements OnChanges {

  @Input() modalTitle: string = '';
  @Input()  product = {} as Product;
  @Output() successEvent = new EventEmitter<void>(); // Event khi nhấn yes
  @Output() cancelEvent = new EventEmitter<void>();


  defaultProduct: Product = {} as Product;

  constructor(private productService: ProductService,
    private snackBarService: SnackBarService,
    private errorHandle: ErrorHandleService,
  ){}

  ngOnChanges(changes: SimpleChanges): void {
      this.defaultProduct = {...this.product}
  }

  saveProduct() {
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

  onSuccess(){
    this.successEvent.emit();
  }
  onCancel(){
    this.cancelEvent.emit();
  }

  onReset(){
    this.product = {...this.defaultProduct}
  }

}
