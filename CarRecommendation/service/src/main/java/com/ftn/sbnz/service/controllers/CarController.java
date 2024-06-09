package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.DTO.CarDTO;
import com.ftn.sbnz.model.mapping.CarMapper;
import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import com.ftn.sbnz.service.services.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cars")
public class CarController {
	private final ICarService carService;

	@Autowired
	public CarController(ICarService carService) {
		this.carService = carService;
	}

	@PostMapping("/populate")
	public ResponseEntity<List<CarDTO>> populateCars(@RequestBody List<CarDTO> carsDTO) {
		List<Car> cars = carService.saveAll(CarMapper.INSTANCE.carsDTOToCars(carsDTO));
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(cars));
	}

	@GetMapping("/recommendations")
	public ResponseEntity<List<CarDTO>> getCarRecommendations(@RequestParam CarPreferenceType carPreferenceType) {
		List<Car> recommendedCars = carService.getCarRecommendations(carPreferenceType);
		return ResponseEntity.ok(CarMapper.INSTANCE.carsToCarsDTO(recommendedCars));
	}

//	@GetMapping("/filter")
//	public List<Car> filterCars(@RequestBody CarFilterCriteria filterCriteria) {
//        return carService.filterCars(filterCriteria);
//	}
}
