package com.openclassrooms.estate_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.estate_back_end.dto.LoginRequest;
import com.openclassrooms.estate_back_end.dto.RegisterRequest;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.service.JWTService;
import com.openclassrooms.estate_back_end.service.UserService;
import com.openclassrooms.estate_back_end.dto.AuthResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        User user = userService.registerUser(request.getEmail(), request.getName(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token)); // 200 ok
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token)); // 200 ok
    }

}
