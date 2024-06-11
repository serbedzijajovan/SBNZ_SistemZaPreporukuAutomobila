package com.ftn.sbnz.model.models.drools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.type.Position;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GraphItem {
    @Position(0)
    String child;
    @Position(1)
    String parent;
    @Position(2)
    String type;
}
