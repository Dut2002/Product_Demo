import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FixSystemComponent } from './fix-system.component';

describe('FixSystemComponent', () => {
  let component: FixSystemComponent;
  let fixture: ComponentFixture<FixSystemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FixSystemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FixSystemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
