import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductVoucherComponent } from './product-voucher.component';

describe('ProductVoucherComponent', () => {
  let component: ProductVoucherComponent;
  let fixture: ComponentFixture<ProductVoucherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductVoucherComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductVoucherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
