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
public class CarScore implements Serializable {
    private CarRecommendationCriteria carRecommendationCriteria;
    private Car car;
    private int score;

    public void addPoints(int points) {
        this.score += points;
    }

    public boolean isCriteriaMet(int criteria)
    {
        switch (criteria) {
            case 1:
                return carRecommendationCriteria.getPrefersFuelEfficiency();
            case 2:
                return carRecommendationCriteria.getPrefersFamilyFriendly();
            case 3:
                return carRecommendationCriteria.getPrefersPerformance();
        }

        return false;
    }
}
