package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.UserDTO;
import com.openclassrooms.estate_back_end.exception.EntityNotFoundException;
import com.openclassrooms.estate_back_end.mapper.UserMapper;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.repository.UserRepository;
import com.openclassrooms.estate_back_end.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Tag(name = "User APIs", description = "APIs for retrieving user data")
    @Operation(summary = "Get current authenticated user",
        description = "Retrieve the currently authenticated user details "
            + "based on the provided authentication token.")
    @ApiResponses(
        value = {@ApiResponse(responseCode = "200", description = "Successfully fetched the current user's data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")})
    @GetMapping("/auth/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + username));
        UserDTO userDTO = userMapper.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Tag(name = "User APIs", description = "APIs for retrieving user data by ID")
    @Operation(summary = "Get user by ID", description = "Retrieve the details of a user by their unique user ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully fetched the user data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")})
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "Unique ID of the user to retrieve",
        required = true, example = "1") @PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        UserDTO userDTO = userMapper.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

}