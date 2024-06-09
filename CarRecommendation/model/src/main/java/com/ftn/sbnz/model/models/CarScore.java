package com.ftn.sbnz.model.models;

import java.io.Serializable;

import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarScore implements Serializable {
    private CarPreferenceType carPreferenceType;
    private Car car;
    private int score;

    public void addPoints(int points) {
        this.score += points;
    }
}
