import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySupplierRequestDetailComponent } from './my-supplier-request-detail.component';

describe('MySupplierRequestDetailComponent', () => {
  let component: MySupplierRequestDetailComponent;
  let fixture: ComponentFixture<MySupplierRequestDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MySupplierRequestDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MySupplierRequestDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
