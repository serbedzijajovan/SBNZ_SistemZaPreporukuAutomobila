package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.CarFilterCriteria;
import com.ftn.sbnz.model.models.CarPreferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cars")
public class CarController {
	private final CarService carService;

	@Autowired
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@PostMapping("/populate")
	public ResponseEntity<List<Car>> populateCars(@RequestBody List<Car> cars) {
		System.out.println("Number of cars: " + cars.size());
		carService.setCars(cars);
		return ResponseEntity.ok(cars);
	}

	@GetMapping("/recommendations")
	public List<Car> getCarRecommendations(@RequestParam CarPreferenceType carPreferenceType) {
		return carService.getCarRecommendationsTemplate(carPreferenceType);
	}

	@GetMapping("/filter")
	public List<Car> filterCars(@RequestBody CarFilterCriteria filterCriteria) {
        return carService.filterCars(filterCriteria);
	}
}
