package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.dto.CarDTO;
import com.ftn.sbnz.model.mapping.CarMapper;
import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import com.ftn.sbnz.service.services.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/cars")
public class CarController {
	private final ICarService carService;

	@Autowired
	public CarController(ICarService carService) {
		this.carService = carService;
	}

	@GetMapping("/all")
	public ResponseEntity<List<CarDTO>> getAll() {
		List<Car> cars = carService.findAll();
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(cars));
	}

	@GetMapping("/make-report")
	public ResponseEntity<Object> gerMakeInfo() {
		carService.numberOfCarsForMake();

		return ResponseEntity.status(HttpStatus.OK).body("Liked successfully!");
	}

	@PostMapping("/populate")
	public ResponseEntity<List<CarDTO>> populateCars(@RequestBody List<CarDTO> carsDTO) {
		List<Car> cars = carService.saveAll(CarMapper.INSTANCE.carsDTOToCars(carsDTO));
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(cars));
	}

	@GetMapping("/filter-custom")
	public ResponseEntity<List<CarDTO>> filterCarsCustom(@RequestParam String make,
												   @RequestParam String model,
												   @RequestParam int hp) {
		System.out.println(make);
		System.out.println(model);
		System.out.println(hp);
		List<Car> filtered = carService.filterCarsByCustom(make, model, hp);
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(filtered));
	}

	@GetMapping("/recommendations")
	public ResponseEntity<List<CarDTO>> getCarRecommendations(@RequestParam CarPreferenceType carPreferenceType) {
		List<Car> recommendedCars = carService.getCarRecommendations(carPreferenceType);
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(recommendedCars));
	}

	@GetMapping("/filter")
	public ResponseEntity<List<CarDTO>> filterCars(@RequestParam String make) {
		List<Car> cars = carService.filterCars(make);
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(cars));
	}
}
