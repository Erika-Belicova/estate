package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.exception.EntityNotFoundException;
import com.openclassrooms.estate_back_end.exception.RentalCreationException;
import com.openclassrooms.estate_back_end.exception.RentalUpdateException;
import com.openclassrooms.estate_back_end.exception.UnauthorizedAccessException;
import com.openclassrooms.estate_back_end.mapper.RentalMapper;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.response.MessageResponse;
import com.openclassrooms.estate_back_end.response.RentalsResponse;
import com.openclassrooms.estate_back_end.service.PictureService;
import com.openclassrooms.estate_back_end.service.RentalService;
import com.openclassrooms.estate_back_end.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@SecurityRequirement(name = "Authorization")
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
    public ResponseEntity<RentalsResponse> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        RentalsResponse response = new RentalsResponse(rentals);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Rental APIs", description = "APIs for creating new rentals")
    @Operation(summary = "Create new rental", description = "Create a new rental with the provided details such as name, surface, price, a picture and a description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createRental(
            @Parameter(description = "Name of the rental", required = true, example = "Apartment")
            @RequestParam("name") String name,

            @Parameter(description = "Surface area of the rental in square meters", required = true, example = "50")
            @RequestParam("surface")
            @DecimalMin(value = "0", inclusive = true, message = "Surface can not be a negative value")
            BigDecimal surface,

            @Parameter(description = "Price of the rental per night", required = true, example = "200")
            @RequestParam("price")
            @DecimalMin(value = "0", inclusive = true, message = "Price can not be a negative value")
            BigDecimal price,

            @Parameter(description = "Picture of the rental (picture file)", required = true)
            @RequestParam("picture") MultipartFile picture,

            @Parameter(description = "Description of the rental", required = true, example = "A nice apartment")
            @RequestParam("description") String description) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User owner = userService.getUserByEmail(currentUsername);

        if (owner == null) {
            throw new EntityNotFoundException("User not found");
        }

        String picturePath = pictureService.uploadPicture(picture); // upload picture
        String pictureUrl = pictureService.getPicture(picturePath); // get url for picture to save in the database

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setOwnerId(owner.getUserId());
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
        return ResponseEntity.ok(new MessageResponse("Rental created !"));
    }

    @Tag(name = "Rental APIs", description = "APIs for updating existing rentals")
    @Operation(summary = "Update existing rental", description = "Update the details of an existing rental, such as name, surface, price, and description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authorized to update the rental")
    })
    @PutMapping("/rentals/{id}")
    public ResponseEntity<Object> updateRental(
            @Parameter(description = "Unique ID of the rental to update", required = true, example = "2")
            @PathVariable Integer id,

            @Parameter(description = "Updated name of the rental", example = "House")
            @RequestParam(value = "name", required = false) String name,

            @Parameter(description = "Updated surface area of the rental in square meters", example = "90")
            @RequestParam(value = "surface", required = false)
            @DecimalMin(value = "0", inclusive = true, message = "Surface can not be a negative value")
            BigDecimal surface,

            @Parameter(description = "Updated price of the rental per night", example = "400")
            @RequestParam(value = "price", required = false)
            @DecimalMin(value = "0", inclusive = true, message = "Price can not be a negative value")
            BigDecimal price,

            @Parameter(description = "Updated description", example = "A nice house")
            @RequestParam(value = "description", required = false) String description) throws IOException {

        Rental rental = rentalService.getRentalEntityById(id);

        if (rental == null) {
            throw new EntityNotFoundException("Rental not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // check if user is the owner of the rental
        if (!rental.getOwner().getEmail().equals(currentUsername)) {
            throw new UnauthorizedAccessException("You are not authorized to update this rental");
        }
        // update only provided values
        if (name != null) rental.setName(name);
        if (surface != null) rental.setSurface(surface);
        if (price != null) rental.setPrice(price);
        if (description != null) rental.setDescription(description);

        try {
            rentalService.updateRental(rental);
        } catch (Exception ex) {
            throw new RentalUpdateException("Error updating rental", ex);
        }
        return ResponseEntity.ok(new MessageResponse("Rental updated !"));
    }

    @Tag(name = "Rental APIs", description = "APIs for retrieving rental details by ID")
    @Operation(summary = "Get rental by ID", description = "Retrieve the details of a specific rental based on its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the rental data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> getRentalById(
            @Parameter(description = "Unique ID of the rental to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        RentalDTO rentalDTO = rentalService.getRentalById(id);

        if (rentalDTO == null) {
            throw new EntityNotFoundException("Rental not found with ID: " + id);
        }
        return ResponseEntity.ok(rentalDTO);
    }
}