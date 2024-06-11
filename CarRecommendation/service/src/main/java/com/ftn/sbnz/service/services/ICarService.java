package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;

import java.util.List;

public interface ICarService extends IBaseService<Car> {
    List<Car> filterCars(String make, String model, int year);

    List<Car> getCarRecommendations(CarPreferenceType carPreferenceType);

    void populateBWSession();

    List<Car> filterCars(String make);

    void numberOfCarsForMake();

    List<Car> filterCarsByCustom(String make, String model, int hp);
}
