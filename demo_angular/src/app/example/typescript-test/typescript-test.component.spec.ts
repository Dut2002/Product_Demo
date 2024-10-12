import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypescriptTestComponent } from './typescript-test.component';

describe('TypescriptTestComponent', () => {
  let component: TypescriptTestComponent;
  let fixture: ComponentFixture<TypescriptTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TypescriptTestComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TypescriptTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
