package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.dto.LoginResponseDTO;
import com.ftn.sbnz.model.dto.LoginUserDTO;
import com.ftn.sbnz.model.dto.RegisterUserDTO;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.service.security.JwtService;
import com.ftn.sbnz.service.services.AuthenticationService;
import com.ftn.sbnz.service.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;
    private final IUserService userService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, IUserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterUserDTO registerUserDto) {
        if (userService.userExists(registerUserDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already taken!");
        }
        authenticationService.signup(registerUserDto);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponseDTO loginResponse = new LoginResponseDTO(authenticatedUser.getUsername(), jwtToken);
        return ResponseEntity.ok(loginResponse);
    }
}
