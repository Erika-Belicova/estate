package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.UserDTO;
import com.openclassrooms.estate_back_end.mapper.UserMapper;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.repository.UserRepository;
import com.openclassrooms.estate_back_end.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDTO userDTO = userMapper.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

}