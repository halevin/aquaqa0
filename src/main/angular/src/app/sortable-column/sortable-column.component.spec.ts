import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SortableColumnComponentComponent } from './sortable-column-component.component';

describe('SortableColumnComponentComponent', () => {
  let component: SortableColumnComponentComponent;
  let fixture: ComponentFixture<SortableColumnComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SortableColumnComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SortableColumnComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
