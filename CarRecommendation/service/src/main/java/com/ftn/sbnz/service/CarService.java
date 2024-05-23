package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.CarFilterCriteria;
import com.ftn.sbnz.model.models.CarRecommendationCriteria;
import com.ftn.sbnz.model.models.CarScore;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final KieContainer kieContainer;

    public static List<Car> cars = new ArrayList<>();
    int scoreThreshold = 15;

    @Autowired
    public CarService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public void setCars(List<Car> cars) {
        CarService.cars = cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getCarRecommendations(CarRecommendationCriteria criteria) {
        KieSession kieSession = kieContainer.newKieSession();

        // Initialize CarScore objects for each car with an initial score of 0
        List<CarScore> carScores = cars.stream()
                .map(car -> new CarScore(criteria, car, 0))
                .collect(Collectors.toList());

        try {
            // Insert CarScore objects into the session
            for (CarScore carScore : carScores) {
                kieSession.insert(carScore);
            }

            // Insert Criteria into the session
            kieSession.insert(criteria);

            // Fire all rules in the session
            kieSession.fireAllRules();

            // Collect and return cars that meet the score criteria specified in CarRecommendationCriteria
            return carScores.stream()
                    .filter(carScore -> carScore.getScore() >= scoreThreshold)
                    .map(CarScore::getCar)
                    .collect(Collectors.toList());
        } finally {
            if (kieSession != null) {
                kieSession.dispose();  // Ensure the session is disposed of after use
            }
        }
    }

    public List<Car> filterCars(CarFilterCriteria carFilterCriteria) {
        return List.of();
    }
}
