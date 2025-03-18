package com.openclassrooms.estate_back_end.controller;

import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.mapper.RentalMapper;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import com.openclassrooms.estate_back_end.service.RentalService;
import com.openclassrooms.estate_back_end.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final UserService userService;

    @Autowired
    public RentalController(RentalService rentalService, RentalMapper rentalMapper, UserService userService) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.userService = userService;
    }

    @GetMapping("/rentals")
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(Collections.singletonMap("rentals", rentals));
    }

    @PostMapping("/rentals")
    public ResponseEntity<Object> createRental(@Valid @RequestBody RentalDTO rentalDTO) {
        Integer ownerId = rentalDTO.getOwnerId();
        User owner = userService.getUserById(ownerId);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Owner not found");
        }
        Rental rental = rentalMapper.toRentalEntity(rentalDTO, owner);
        rentalService.createRental(rental);
        return ResponseEntity.ok(Collections.singletonMap("message", "Rental created !"));
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rentalDTO = rentalService.getRentalById(id);
        return ResponseEntity.ok(rentalDTO);
    }
}