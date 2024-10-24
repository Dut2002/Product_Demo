import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolePermissionDetailComponent } from './role-permission-detail.component';

describe('RolePermissionDetailComponent', () => {
  let component: RolePermissionDetailComponent;
  let fixture: ComponentFixture<RolePermissionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RolePermissionDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RolePermissionDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
