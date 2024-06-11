package com.ftn.sbnz.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("createdAt")
public class TooManyLikesEvent implements Serializable {
    private final Long userId;

    private Date createdAt;

    public TooManyLikesEvent(Long userId) {
        this.userId = userId;
        this.createdAt = new Date();
    }
}
