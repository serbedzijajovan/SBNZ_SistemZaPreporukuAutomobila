package com.ftn.sbnz.service.services.implementations;

import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import com.ftn.sbnz.model.models.CarScore;
import com.ftn.sbnz.service.repositories.BaseJPARepository;
import com.ftn.sbnz.service.repositories.CarRepository;
import com.ftn.sbnz.service.services.ICarService;
import com.ftn.sbnz.service.services.KieService;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarService extends BaseService<Car> implements ICarService {
    private final CarRepository carRepository;
    private final KieService kieService;

    int scoreThreshold = 15;

    @Autowired
    public CarService(CarRepository carRepository, KieService kieService) {
        this.carRepository = carRepository;
        this.kieService = kieService;
    }

    @Override
    protected BaseJPARepository<Car> getRepository() {
        return carRepository;
    }

    @Override
    public List<Car> getCarRecommendations(CarPreferenceType carPreferenceType) {
        List<Car> cars = findAll();
        List<CarScore> carScores = cars.stream()
                .map(car -> new CarScore(carPreferenceType, car, 0))
                .collect(Collectors.toList());

        String templatePath = "/rules/recommendation/car_recommendation.drt";
        String dataPath = "/rules/recommendation/car_recommendation.xlsx";
        KieSession kieSession = kieService.createKieSessionFromSpreadsheet(templatePath, dataPath);

        // Insert CarScore objects into the session
        for (CarScore carScore : carScores) {
            kieSession.insert(carScore);
        }

        // Fire all rules in the session
        kieSession.fireAllRules();

        // Sort by score in descending order, filter by threshold and uniqueness, and limit to top 50
        Set<String> uniqueMakeModel = new HashSet<>();
        return carScores.stream()
                .sorted(Comparator.comparingInt(CarScore::getScore).reversed())
                .filter(carScore -> carScore.getScore() >= scoreThreshold)
                .map(CarScore::getCar)
                .filter(car -> uniqueMakeModel.add(car.getMake() + "-" + car.getModel()))
                .limit(50)
                .collect(Collectors.toList());

    }
}
