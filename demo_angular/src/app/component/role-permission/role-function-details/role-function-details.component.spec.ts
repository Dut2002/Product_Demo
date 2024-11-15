import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleFunctionDetailsComponent } from './role-function-details.component';

describe('RoleFunctionDetailsComponent', () => {
  let component: RoleFunctionDetailsComponent;
  let fixture: ComponentFixture<RoleFunctionDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RoleFunctionDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleFunctionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
