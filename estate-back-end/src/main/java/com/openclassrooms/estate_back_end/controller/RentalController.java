package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.exception.RentalCreationException;
import com.openclassrooms.estate_back_end.mapper.RentalMapper;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.service.PictureService;
import com.openclassrooms.estate_back_end.service.RentalService;
import com.openclassrooms.estate_back_end.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final UserService userService;
    private final PictureService pictureService;

    @Autowired
    public RentalController(
            RentalService rentalService, RentalMapper rentalMapper,
            UserService userService, PictureService pictureService) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.userService = userService;
        this.pictureService = pictureService;
    }

    @Tag(name = "Rental APIs", description = "APIs for retrieving rental listings")
    @Operation(summary = "Get all rentals", description = "Retrieve a list of all available rentals.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of rentals"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @GetMapping("/rentals")
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(Collections.singletonMap("rentals", rentals));
    }

    @Tag(name = "Rental APIs", description = "APIs for creating new rentals")
    @Operation(summary = "Create new rental", description = "Create a new rental with the provided details such as name, surface, price, a picture and a description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @PostMapping("/rentals")
    public ResponseEntity<Object> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User owner = userService.getUserByEmail(currentUsername);
        Integer ownerId = owner.getUserId();

        String picturePath = pictureService.uploadPicture(picture); // upload picture
        String pictureUrl = pictureService.getPicture(picturePath); // get url for picture to save in the database

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setOwnerId(ownerId);
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setDescription(description);
        rentalDTO.setPicture(Collections.singletonList(pictureUrl));

        Rental rental = rentalMapper.toRentalEntity(rentalDTO, owner);

        // try creating rental
        try {
            rentalService.createRental(rental);
        } catch (Exception e) {
            throw new RentalCreationException("Error creating rental", e);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Rental created !"));
    }

    @Tag(name = "Rental APIs", description = "APIs for updating existing rentals")
    @Operation(summary = "Update existing rental", description = "Update the details of an existing rental, such as name, surface, price, and description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authorized to update the rental")
    })
    @PutMapping("/rentals/{id}")
    public ResponseEntity<Object> updateRental(
            @PathVariable Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surface", required = false) BigDecimal surface,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        Rental rental = rentalService.getRentalEntityById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // check if user is the owner of the rental
        if (!rental.getOwner().getEmail().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this rental");
        }
        // update only provided values
        if (name != null) rental.setName(name);
        if (surface != null) rental.setSurface(surface);
        if (price != null) rental.setPrice(price);
        if (description != null) rental.setDescription(description);

        rentalService.updateRental(rental);
        return ResponseEntity.ok(Collections.singletonMap("message", "Rental updated !"));
    }

    @Tag(name = "Rental APIs", description = "APIs for retrieving rental details by ID")
    @Operation(summary = "Get rental by ID", description = "Retrieve the details of a specific rental based on its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the rental data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rentalDTO = rentalService.getRentalById(id);
        return ResponseEntity.ok(rentalDTO);
    }
}