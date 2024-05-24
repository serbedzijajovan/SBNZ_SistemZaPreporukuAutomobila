package com.ftn.sbnz.model.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransmissionType {
    @JsonProperty("Manual")
    MANUAL,
    @JsonProperty("Automatic")
    AUTOMATIC,
    @JsonProperty("Continuously variable transmission (CVT)")
    CVT,
    @JsonProperty("robot")
    ROBOT
}
