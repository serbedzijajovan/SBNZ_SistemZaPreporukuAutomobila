package com.ftn.sbnz.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Role(Role.Type.EVENT)
public class ShouldBanUserEvent implements Serializable {
    private final Long userId;
}
