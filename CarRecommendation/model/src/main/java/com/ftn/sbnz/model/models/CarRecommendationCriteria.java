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
public class CarRecommendationCriteria implements Serializable {
    private Boolean prefersFuelEfficiency;
    private Boolean prefersFamilyFriendly;
    private Boolean prefersPerformance;
}
