package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.models.enums.BodyType;
import com.ftn.sbnz.model.models.enums.EngineType;
import com.ftn.sbnz.model.models.enums.TransmissionType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO implements Serializable {
    private Long id;
    private String make;
    private String model;
    private Integer yearFrom;
    private Integer yearTo;
    private BodyType bodyType;
    private Integer numberOfSeats;
    private Double lengthMm;
    private Double widthMm;
    private Double heightMm;
    private Double wheelbaseMm;
    private Integer curbWeightKg;
    private Integer engineHp;
    private EngineType engineType;
    private String driveWheels;
    private TransmissionType transmission;
    private Integer fuelTankCapacityL;
    private Double mixedFuelConsumptionPer100KmL;
    private Integer maxSpeedKmPerH;
    private Double cityFuelPer100KmL;
    private Double highwayFuelPer100KmL;
    private Double acceleration0100KmHS;
}
