package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.enums.CarFilterCriteria;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import com.ftn.sbnz.model.models.CarScore;

import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceDROOLS {
    private final KieContainer kieContainer;

    public static List<Car> cars = new ArrayList<>();
    int scoreThreshold = 15;

    @Autowired
    public CarServiceDROOLS(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public void setCars(List<Car> cars) {
        CarServiceDROOLS.cars = cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getCarRecommendations(CarPreferenceType criteria) {
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

    public List<Car> getCarRecommendationsTemplate(CarPreferenceType carPreferenceType) {
        KieSession kieSession = createKieSessionFromTemplate();

        List<CarScore> carScores = cars.stream()
                .map(car -> new CarScore(carPreferenceType, car, 0))
                .collect(Collectors.toList());

        try {
            // Insert CarScore objects into the session
            for (CarScore carScore : carScores) {
                kieSession.insert(carScore);
            }

            // Fire all rules in the session
            kieSession.fireAllRules();

            Set<String> uniqueMakeModel = new HashSet<>(); // Set to track unique make and model combinations

            // Sort by score in descending order, filter by threshold and uniqueness, and limit to top 50
            return carScores.stream()
                    .sorted(Comparator.comparingInt(CarScore::getScore).reversed())
                    .filter(carScore -> carScore.getScore() >= scoreThreshold)
                    .map(CarScore::getCar)
                    .filter(car -> uniqueMakeModel.add(car.getMake() + "-" + car.getModel()))
                    .limit(50)
                    .collect(Collectors.toList());
        } finally {
            if (kieSession != null) {
                kieSession.dispose();  // Ensure the session is disposed of after use
            }
        }
    }

    /**
     * Tests customer-classification-simple.drt template by manually creating
     * the corresponding DRL using a bidimensional array of Strings
     * as the data source.
     */
    public KieSession createKieSessionFromTemplate() {
        ClassPathResource classPathResource = new ClassPathResource("/rules/template_rules/template.drt");
        InputStream template;
        try {
            template = classPathResource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{"CarPreferenceType.SPORT", "engineHp", ">=", "250", "20"},
                new String[]{"CarPreferenceType.SPORT", "acceleration0100KmHS", "<=", "6.0", "20"},
                new String[]{"CarPreferenceType.SPORT", "transmission", "==", "TransmissionType.MANUAL", "5"},
                new String[]{"CarPreferenceType.SPORT", "driveWheels", "==", "DriveWheels.RWD", "10"},
                new String[]{"CarPreferenceType.SPORT", "bodyType", "in", "(BodyType.CABRIOLET, BodyType.COUPE, BodyType.ROADSTER, BodyType.SEDAN)", "5"},

                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "numberOfSeats", ">=", "5", "10"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "bodyType", "in", "(BodyType.CROSSOVER, BodyType.HATCHBACK, BodyType.MINIVAN, BodyType.SEDAN, BodyType.WAGON)", "20"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "curbWeightKg", ">=", "1500", "15"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "mixedFuelConsumptionPer100KmL", "<=", "8.0", "15"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "yearFrom", ">=", "2015", "20"},

                new String[]{"CarPreferenceType.OFF_ROAD", "heightMm", ">=", "1500", "15"},
                new String[]{"CarPreferenceType.OFF_ROAD", "driveWheels", "in", "(DriveWheels.AWD, DriveWheels.FOURWD)", "20"},
                new String[]{"CarPreferenceType.OFF_ROAD", "mixedFuelConsumptionPer100KmL", "<=", "8.0", "15"},
                new String[]{"CarPreferenceType.OFF_ROAD", "curbWeightKg", ">", "2000", "10"},
                new String[]{"CarPreferenceType.OFF_ROAD", "engineType", ">", "EngineType.DIESEL", "10"},
        });

        DataProviderCompiler converter = new DataProviderCompiler();
        String drl = converter.compile(dataProvider, template);

        return createKieSessionFromDRL(drl);
    }

    private KieSession createKieSessionFromDRL(String drl) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: " + message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper.build().newKieSession();
    }
}
