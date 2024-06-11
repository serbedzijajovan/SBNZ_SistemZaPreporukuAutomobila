import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterBackwardCarsComponent } from './filter-backward-cars.component';

describe('FilterBackwardCarsComponent', () => {
  let component: FilterBackwardCarsComponent;
  let fixture: ComponentFixture<FilterBackwardCarsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FilterBackwardCarsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilterBackwardCarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
