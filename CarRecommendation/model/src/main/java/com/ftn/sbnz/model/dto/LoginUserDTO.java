package com.ftn.sbnz.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO implements Serializable {
    private String email;
    private String password;
}
