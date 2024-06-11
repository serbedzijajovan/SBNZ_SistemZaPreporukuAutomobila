package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.dto.CarLikeRequestDTO;
import com.ftn.sbnz.model.dto.LoginResponseDTO;
import com.ftn.sbnz.model.models.CarLike;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.model.models.enums.CarPreferenceType;
import com.ftn.sbnz.service.services.implementations.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        LoginResponseDTO loginResponse = new LoginResponseDTO(currentUser.getEmail(), "");

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/like-car")
    public ResponseEntity<Object> createCarFollow(@RequestBody CarLikeRequestDTO carLikeRequestDTO) {
        if (userService.createCarLike(carLikeRequestDTO.getEmail(), carLikeRequestDTO.getCarId())) {
            return ResponseEntity.status(HttpStatus.OK).body("Liked successfully!");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed to like this car!");
    }

    @GetMapping("/unban")
    public ResponseEntity<Object> unban(@RequestParam String email) {
        userService.unbanUser(email);
        return ResponseEntity.status(HttpStatus.OK).body("Finished!");
    }
}
