package com.ftn.sbnz.model.models;

import java.io.Serializable;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car implements Serializable {
    private Long id;
    private String make;  // Brand of the car
    private String model;  // Specific model of the car
    private Integer yearFrom;  // Model year start
    private Integer yearTo;  // Model year end
    private String bodyType;  // Type of the car body (e.g., sedan, SUV, hatchback)
    private String numberOfSeats;  // Seating capacity
    private Double lengthMm;  // Length of the car
    private Double widthMm;  // Width of the car
    private Double heightMm;  // Height of the car
    private Double wheelbaseMm;  // Wheelbase of the car
    private Integer curbWeightKg;  // Weight of the car without passengers or cargo
    private Integer engineHp;  // Horsepower, indicating the car’s power
    private String engineType;  // Type of engine (e.g., petrol, diesel, electric)
    private String driveWheels;  // Drive configuration (e.g., front-wheel drive, all-wheel drive)
    private String transmission;  // Type of transmission (e.g., manual, automatic)
    private Integer fuelTankCapacityL;  // Size of the fuel tank
    private Double mixedFuelConsumptionPer100KmL;  // Average fuel consumption
    private Integer maxSpeedKmPerH;  // Maximum speed
    private Double cityFuelPer100KmL;  // City fuel consumption
    private Double highwayFuelPer100KmL;  // Highway fuel consumption
    private Double acceleration0100KmHS;  // Time taken to accelerate from 0 to 100 km/h

}