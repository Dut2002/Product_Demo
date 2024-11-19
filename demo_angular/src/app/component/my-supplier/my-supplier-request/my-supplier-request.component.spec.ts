import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySupplierRequestComponent } from './my-supplier-request.component';

describe('MySupplierRequestComponent', () => {
  let component: MySupplierRequestComponent;
  let fixture: ComponentFixture<MySupplierRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MySupplierRequestComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MySupplierRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
