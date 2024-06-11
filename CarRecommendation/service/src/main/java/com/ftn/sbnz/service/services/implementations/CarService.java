package com.ftn.sbnz.service.services.implementations;

import com.ftn.sbnz.model.events.UnbanUserEvent;
import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.drools.GraphItem;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import com.ftn.sbnz.model.models.CarScore;
import com.ftn.sbnz.service.repositories.BaseJPARepository;
import com.ftn.sbnz.service.repositories.CarRepository;
import com.ftn.sbnz.service.services.ICarService;
import com.ftn.sbnz.service.services.KieService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ftn.sbnz.service.services.KieService.backwardKsession;
import static com.ftn.sbnz.service.services.KieService.cepKsession;

@Service
public class CarService extends BaseService<Car> implements ICarService {
    private final CarRepository carRepository;
    private final KieService kieService;
    private final KieContainer kieContainer;

    int scoreThreshold = 15;

    @Autowired
    public CarService(CarRepository carRepository, KieService kieService, KieContainer kieContainer) {
        this.carRepository = carRepository;
        this.kieService = kieService;
        this.kieContainer = kieContainer;
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

    public List<Car> filterCars(String make, String model, int year) {
        return carRepository.findByMakeAndModelAndYearFrom(make, model, year);
    }

    @Override
    public void populateBWSession() {

    }

    @Override
    public List<Car> filterCars(String make) {
        backwardKsession = kieContainer.newKieSession("bwKsession");

        // Initialize the global list
        List<String> resultList = new ArrayList<>();
        backwardKsession.setGlobal("param", make);
        backwardKsession.setGlobal("resultList", resultList);

        List<Car> cars = carRepository.findAll();

        // Create a map to store models for each make
        Map<String, Set<String>> makeModelsMap = new HashMap<>();

        for (Car car : cars) {
            String carMake = car.getMake();
            String carModel = car.getModel();

            // Check if the model is already added for this make
            if (!makeModelsMap.containsKey(carMake)) {
                makeModelsMap.put(carMake, new HashSet<>());
            }

            Set<String> modelsForMake = makeModelsMap.get(carMake);
            if (!modelsForMake.contains(carModel)) {
                GraphItem modelToMake = new GraphItem(carMake + "_" + carModel, carMake, "Model");
                backwardKsession.insert(modelToMake);
                modelsForMake.add(carModel);
            }

            GraphItem carToModel = new GraphItem(car.getId().toString(), carMake + "_" + carModel, "Car");
            backwardKsession.insert(carToModel);
        }

        backwardKsession.fireAllRules();
        backwardKsession.dispose();

        // Convert the result list from String to Long
        List<Long> carIds = resultList.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return carRepository.findByIdIn(carIds);
    }

    @Override
    public void numberOfCarsForMake() {
        KieSession accKSession = kieContainer.newKieSession("accKsession");

        List<Car> cars = carRepository.findAll();

        // Insert the list of cars into the session
        for (Car car : cars) {
            accKSession.insert(car);
        }

        // Fire the rules
        accKSession.fireAllRules();

        // Dispose the session
        accKSession.dispose();
    }

    @Override
    public List<Car> filterCarsByCustom(String make, String model, int hp) {
        KieSession accKSession = kieContainer.newKieSession("fwdKsession");
        accKSession.setGlobal("makeFilter", make);
        accKSession.setGlobal("modelFilter", model);
        accKSession.setGlobal("hpFilter", hp);

        List<Car> filteredCars = new ArrayList<>();
        accKSession.setGlobal("filteredCars", filteredCars);

        // Insert the list of cars into the session
        List<Car> cars = carRepository.findAll();
        for (Car car : cars) {
            accKSession.insert(car);
        }

        // Fire the rules
        accKSession.fireAllRules();

        // Dispose the session
        accKSession.dispose();

        return filteredCars;
    }
}
