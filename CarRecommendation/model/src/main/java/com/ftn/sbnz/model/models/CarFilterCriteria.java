package com.ftn.sbnz.model.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarFilterCriteria implements Serializable {
    private String make;
    private String model;
    private Integer year;
    private Double minPrice;
    private Double maxPrice;
}
