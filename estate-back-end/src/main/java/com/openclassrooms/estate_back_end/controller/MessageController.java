package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.MessageDTO;
import com.openclassrooms.estate_back_end.mapper.MessageMapper;
import com.openclassrooms.estate_back_end.model.Message;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.service.MessageService;
import com.openclassrooms.estate_back_end.service.RentalService;
import com.openclassrooms.estate_back_end.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final RentalService rentalService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageController(MessageService messageService, UserService userService,
                             RentalService rentalService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.userService = userService;
        this.rentalService = rentalService;
        this.messageMapper = messageMapper;
    }

    @Tag(name = "Message APIs", description = "APIs for sending messages")
    @Operation(summary = "Send a message", description = "Send a message from a user to a rental. The message will be associated with the user and rental.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user or token"),
            @ApiResponse(responseCode = "404", description = "User or rental not found")
    })
    @PostMapping("/messages")
    public ResponseEntity<Object> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {

        User user = userService.getUserById(messageDTO.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        Rental rental = rentalService.getRentalEntityById(messageDTO.getRentalId());
        if (rental == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rental not found");
        }

        messageDTO.setUserId(user.getUserId());
        messageDTO.setRentalId(rental.getRentalId());
        Message message = messageMapper.toMessageEntity(messageDTO, user, rental);

        messageService.saveMessage(message);

        Map<String, String> response = Collections.singletonMap("message", "Message sent with success");
        return ResponseEntity.ok().body(response);
    }
}
