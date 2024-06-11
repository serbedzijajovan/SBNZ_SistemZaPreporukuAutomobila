import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecommendCarsComponent } from './recommend-cars.component';

describe('RecommendCarsComponent', () => {
  let component: RecommendCarsComponent;
  let fixture: ComponentFixture<RecommendCarsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecommendCarsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecommendCarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
