import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Voucher } from '../../../model/voucher';

@Component({
  selector: 'app-voucher-detail',
  templateUrl: './voucher-detail.component.html',
  styleUrl: './voucher-detail.component.scss'
})
export class VoucherDetailComponent {
  @Input() voucher = {} as Voucher
  @Output() closeEvent = new EventEmitter<void>();

  onClose(){
    this.closeEvent.emit();
  }
}
