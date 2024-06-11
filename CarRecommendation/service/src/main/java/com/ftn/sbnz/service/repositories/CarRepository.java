package com.ftn.sbnz.service.repositories;

import com.ftn.sbnz.model.models.Car;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends BaseJPARepository<Car> {
    List<Car> findByMakeAndModelAndYearFrom(String make, String model, int yearFrom);
    List<Car> findByIdIn(List<Long> ids);
}
