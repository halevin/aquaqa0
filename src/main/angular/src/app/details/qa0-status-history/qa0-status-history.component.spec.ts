import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Qa0StatusHistoryComponent } from './qa0-status-history.component';

describe('Qa0StatusHistoryComponent', () => {
  let component: Qa0StatusHistoryComponent;
  let fixture: ComponentFixture<Qa0StatusHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Qa0StatusHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Qa0StatusHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
