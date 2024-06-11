package com.ftn.sbnz.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarLikeRequestDTO {
    private String email;
    private Long carId;
}
