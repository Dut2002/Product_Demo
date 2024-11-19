import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySupplierComponent } from './my-supplier.component';

describe('MySupplierComponent', () => {
  let component: MySupplierComponent;
  let fixture: ComponentFixture<MySupplierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MySupplierComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MySupplierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
