import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierRequestDetailsComponent } from './supplier-request-details.component';

describe('SupplierRequestDetailsComponent', () => {
  let component: SupplierRequestDetailsComponent;
  let fixture: ComponentFixture<SupplierRequestDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SupplierRequestDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierRequestDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
