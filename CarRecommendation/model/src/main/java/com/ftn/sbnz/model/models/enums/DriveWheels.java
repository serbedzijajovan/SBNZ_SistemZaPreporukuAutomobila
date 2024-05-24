package com.ftn.sbnz.model.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DriveWheels {
    @JsonProperty("All wheel drive (AWD)")
    AWD,
    @JsonProperty("Four wheel drive (4WD)")
    FOURWD,
    @JsonProperty("Front wheel drive")
    FWD,
    @JsonProperty("Rear wheel drive")
    RWD
}
