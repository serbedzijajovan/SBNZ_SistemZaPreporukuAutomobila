package com.ftn.sbnz.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO implements Serializable {
    private String token;
    private long expiresIn;
}