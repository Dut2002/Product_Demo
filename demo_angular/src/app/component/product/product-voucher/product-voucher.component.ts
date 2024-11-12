import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { ApiStatus, PermissionName } from '../../../constant/api.const.urls';
import { Voucher } from '../../../model/voucher';
import { CommonService } from '../../../service/common/common.service';
import { HttpParams } from '@angular/common/http';
import { ConfirmModalComponent } from '../../common/confirm-modal/confirm-modal.component';
import { VoucherDetailComponent } from '../../voucher/voucher-detail/voucher-detail.component';

@Component({
  selector: 'app-product-voucher',
  templateUrl: './product-voucher.component.html',
  styleUrl: './product-voucher.component.scss'
})
export class ProductVoucherComponent {
  @Input() common!: CommonService
  @Input() vouchers: Voucher[] = [];
  @Input() productId: number = 0;
  @Output() loadProductEvent = new EventEmitter<void>();

  @ViewChild('confirmModal') confirmModal!: ConfirmModalComponent;
  @ViewChild('detailComponent') detailComponent!: VoucherDetailComponent;


  curretVoucher: Voucher = {} as Voucher;
  code = '';
  input = false;


  constructor(
  ) { }

  addVoucherProduct() {
    const endpoint = this.common.getPermission(PermissionName.ADD_VOUCHER_FOR_PRODUCT);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.common.base.post(endpoint, {
      productId: this.productId,
      voucherCode: this.code
    }).subscribe({
      next: response => {
        this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000)
        this.loadProduct();
      },
      error: err => {
        this.common.errorHandle.handle(err);
      }
    })
    this.input = false;
  }

  deleteConfirm(voucher: Voucher) {

    this.curretVoucher = { ...voucher };
    this.confirmModal.showConfirmation = true;
  }

  onDeleteVoucher() {
    const endpoint = this.common.getPermission(PermissionName.DELETE_VOUCHER_OF_PRODUCT);
    if (!endpoint) {
      this.common.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    const params = new HttpParams()
    .set("productId", this.productId)
    .set("voucherId", this.curretVoucher.id);

    this.common.base.delete(endpoint, params).subscribe({
      next: response => {
        this.common.snackBar.show(null, response.content, ApiStatus.SUCCESS, 5000)
        this.loadProduct();

      },
      error: err => {
        this.common.errorHandle.handle(err);
      }
    })
    this.input = false;
  }

  closeConfirm() {
    this.confirmModal.showConfirmation = false;
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
    this.detailComponent.showDetail = true;
  }

  closeModal() {
    this.detailComponent.showDetail = false;
    this.curretVoucher = {} as Voucher
  }
}
