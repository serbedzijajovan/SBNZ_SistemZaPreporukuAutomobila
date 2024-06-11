import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterCustomCarsComponent } from './filter-custom-cars.component';

describe('FilterCustomCarsComponent', () => {
  let component: FilterCustomCarsComponent;
  let fixture: ComponentFixture<FilterCustomCarsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FilterCustomCarsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilterCustomCarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
