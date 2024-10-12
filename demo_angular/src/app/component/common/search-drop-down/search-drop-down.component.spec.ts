import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchDropDownComponent } from './search-drop-down.component';

describe('SearchDropDownComponent', () => {
  let component: SearchDropDownComponent;
  let fixture: ComponentFixture<SearchDropDownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchDropDownComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchDropDownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
