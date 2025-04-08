package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.MessageDTO;
import com.openclassrooms.estate_back_end.exception.EntityNotFoundException;
import com.openclassrooms.estate_back_end.mapper.MessageMapper;
import com.openclassrooms.estate_back_end.model.Message;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.response.MessageResponse;
import com.openclassrooms.estate_back_end.service.MessageService;
import com.openclassrooms.estate_back_end.service.RentalService;
import com.openclassrooms.estate_back_end.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    private final UserService userService;

    private final RentalService rentalService;

    private final MessageMapper messageMapper;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, RentalService rentalService,
            MessageMapper messageMapper) {
        this.messageService = messageService;
        this.userService = userService;
        this.rentalService = rentalService;
        this.messageMapper = messageMapper;
    }

    @Tag(name = "Message APIs", description = "APIs for sending messages")
    @Operation(summary = "Send a message",
            description = "Send a message from a user to a rental. "
                    + "The message will be associated with the user and rental.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user or token"),
            @ApiResponse(responseCode = "400", description = "User or rental not found") })
    @PostMapping("/messages")
    public ResponseEntity<Object> sendMessage(@Parameter(
            description = "Message data containing the user ID, rental ID, " + "and the content of the message",
            required = true) @Valid @RequestBody MessageDTO messageDTO) {

        User user = userService.getUserById(messageDTO.getUserId());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        Rental rental = rentalService.getRentalEntityById(messageDTO.getRentalId());
        if (rental == null) {
            throw new EntityNotFoundException("Rental not found");
        }

        messageDTO.setUserId(user.getUserId());
        messageDTO.setRentalId(rental.getRentalId());
        Message message = messageMapper.toMessageEntity(messageDTO, user, rental);

        messageService.saveMessage(message);
        MessageResponse response = new MessageResponse("Message sent with success");

        return ResponseEntity.ok().body(response);
    }

}
