import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntennaFlagsComponent } from './antenna-flags.component';

describe('AntennaFlagsComponent', () => {
  let component: AntennaFlagsComponent;
  let fixture: ComponentFixture<AntennaFlagsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntennaFlagsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntennaFlagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
