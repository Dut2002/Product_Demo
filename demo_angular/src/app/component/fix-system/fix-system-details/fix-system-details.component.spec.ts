import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FixSystemDetailsComponent } from './fix-system-details.component';

describe('FixSystemDetailsComponent', () => {
  let component: FixSystemDetailsComponent;
  let fixture: ComponentFixture<FixSystemDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FixSystemDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FixSystemDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
