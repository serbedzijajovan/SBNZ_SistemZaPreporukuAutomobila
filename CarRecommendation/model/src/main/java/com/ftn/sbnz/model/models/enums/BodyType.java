package com.ftn.sbnz.model.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BodyType {
    @JsonProperty("Cabriolet")
    CABRIOLET,
    @JsonProperty("Coupe")
    COUPE,
    @JsonProperty("Crossover")
    CROSSOVER,
    @JsonProperty("Hatchback")
    HATCHBACK,
    @JsonProperty("Fastback")
    FASTBACK,
    @JsonProperty("Liftback")
    LIFTBACK,
    @JsonProperty("Minivan")
    MINIVAN,
    @JsonProperty("Limousine")
    LIMOUSINE,
    @JsonProperty("Pickup")
    PICKUP,
    @JsonProperty("Roadster")
    ROADSTER,
    @JsonProperty("Sedan")
    SEDAN,
    @JsonProperty("Targa")
    TARGA,
    @JsonProperty("Wagon")
    WAGON,
    @JsonProperty("hardtop")
    HARDTOP,
}
