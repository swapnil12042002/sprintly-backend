package com.sprintly.backend.controller;

import com.sprintly.backend.dto.LoginRequest;
import com.sprintly.backend.dto.SignUpRequest;
import com.sprintly.backend.dto.UserListResponse;
import com.sprintly.backend.dto.UserResponse;
import com.sprintly.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(
            @Valid
            @RequestBody SignUpRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public String login(
            @Valid
            @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @DeleteMapping("/me")
    public String deleteUser(Authentication authentication) {

        String email = authentication.getName();

        return authService.deleteUser(email);
    }
    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        return authService.getCurrentUser(email);
    }

    @GetMapping("/users")
    public List<UserListResponse> getUsers(Authentication authentication) {

        return authService.getAllUsers();
    }
}