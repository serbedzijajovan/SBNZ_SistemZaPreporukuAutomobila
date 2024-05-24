package com.ftn.sbnz.model.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EngineType {
    @JsonProperty("Diesel")
    DIESEL,
    @JsonProperty("Gasoline")
    GASOLINE,
    @JsonProperty("Gas")
    GAS,
    @JsonProperty("Hybrid")
    HYBRID,
}
