package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;

import java.util.List;

public interface ICarService extends IBaseService<Car> {
    List<Car> getCarRecommendations(CarPreferenceType carPreferenceType);
}
