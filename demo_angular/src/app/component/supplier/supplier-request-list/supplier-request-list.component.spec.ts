import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierRequestListComponent } from './supplier-request-list.component';

describe('SupplierRequestListComponent', () => {
  let component: SupplierRequestListComponent;
  let fixture: ComponentFixture<SupplierRequestListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SupplierRequestListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierRequestListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
