import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileProgressComponent } from './file-progress.component';

describe('FileProgressComponent', () => {
  let component: FileProgressComponent;
  let fixture: ComponentFixture<FileProgressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FileProgressComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FileProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
