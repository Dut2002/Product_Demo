import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.scss'
})
export class ConfirmModalComponent {
  @Input() title = 'Are you sure?';
  showConfirmation = false;
  @Input() isLoading = false;
  @Output() confirmEvent = new EventEmitter<void>(); // Event khi nhấn yes

  constructor() { }

  confirm() {
    this.isLoading = true;
    this.confirmEvent.emit();
    this.showConfirmation = false;
  }

  cancel() {
    this.showConfirmation = false;
  }
}
