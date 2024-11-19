import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySupplierHomeComponent } from './my-supplier-home.component';

describe('MySupplierHomeComponent', () => {
  let component: MySupplierHomeComponent;
  let fixture: ComponentFixture<MySupplierHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MySupplierHomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MySupplierHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
