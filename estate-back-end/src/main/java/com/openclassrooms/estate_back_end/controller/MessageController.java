package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.MessageDTO;
import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.mapper.MessageMapper;
import com.openclassrooms.estate_back_end.model.Message;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.service.MessageService;
import com.openclassrooms.estate_back_end.service.RentalService;
import com.openclassrooms.estate_back_end.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public MessageController(MessageService messageService, UserService userService, RentalService rentalService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.userService = userService;
        this.rentalService = rentalService;
        this.messageMapper = messageMapper;
    }

    @PostMapping("/messages")
    public ResponseEntity<Object> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        User user = userService.getUserById(messageDTO.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        RentalDTO rental = rentalService.getRentalById(messageDTO.getRentalId());
        if (rental == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rental not found");
        }

        Message message = messageMapper.toMessageEntity(messageDTO, user, rental);
        messageService.saveMessage(message);

        Map<String, String> response = Collections.singletonMap("message", "Message sent with success"); // response message
        return ResponseEntity.ok().body(response);
    }
}
