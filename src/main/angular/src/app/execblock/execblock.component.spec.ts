import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecblockComponent } from './execblock.component';

describe('ExecblockComponent', () => {
  let component: ExecblockComponent;
  let fixture: ComponentFixture<ExecblockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecblockComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecblockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
