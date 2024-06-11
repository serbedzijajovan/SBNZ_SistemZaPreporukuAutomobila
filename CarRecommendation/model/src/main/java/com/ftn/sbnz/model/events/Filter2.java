package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.models.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Role(Role.Type.EVENT)
public class Filter2 implements Serializable {
    private final Car car;
}

