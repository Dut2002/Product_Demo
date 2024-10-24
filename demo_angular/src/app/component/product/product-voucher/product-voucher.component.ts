import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Voucher } from '../../../model/voucher';
import { ProductService } from '../../../service/product/product.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { ApiStatus } from '../../../constant/api.const.urls';
import { AuthService } from '../../../service/auth/auth.service';
import { RouterUrl } from '../../../constant/app.const.router';

@Component({
  selector: 'app-product-voucher',
  templateUrl: './product-voucher.component.html',
  styleUrl: './product-voucher.component.scss'
})
export class ProductVoucherComponent {
  @Input() vouchers: Voucher[] = [];
  @Input() productId: number = 0;
  @Output() loadProductEvent = new EventEmitter<void>();
  curretVoucher: Voucher = {} as Voucher;
  showConfirm = false;
  showModal = false;
  code = '';
  input = false;

  permission = RouterUrl;

  constructor(private productService: ProductService,
    private snackBarService: SnackBarService,
    private errorHandle: ErrorHandleService,
    public authService: AuthService,
  ) { }

  addVoucherProduct() {
    this.productService.addVoucher(this.productId, this.code).subscribe({
      next: response => {
        this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000)
        this.loadProduct();
      },
      error: err => {
        this.errorHandle.handle(err);
      }
    })
    this.input = false;
  }

  deleteConfirm(voucher: Voucher) {

    this.curretVoucher = { ...voucher };
    this.showConfirm = true;
  }

  onDeleteVoucher() {
    this.productService.deleteVoucher(this.productId, this.curretVoucher.id).subscribe({
      next: response => {
        this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000)
        this.loadProduct();

      },
      error: err => {
        this.errorHandle.handle(err);
      }
    })
    this.input = false;
  }

  closeConfirm() {
    this.showConfirm = false;
    this.curretVoucher = {} as Voucher;
  }

  loadProduct() {
    this.loadProductEvent.emit();
  }

  openInput() {
    this.input = true;
    this.code = '';
  }

  openModal(voucher: Voucher) {
    this.curretVoucher = { ...voucher }
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.curretVoucher = {} as Voucher
  }
}
