package com.openclassrooms.estate_back_end.service;

import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.mapper.RentalMapper;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    @Autowired
    public RentalService(RentalRepository rentalRepository, RentalMapper rentalMapper) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
    }

    public List<RentalDTO> getAllRentals() {
        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();
        return rentalMapper.toRentalDTOList(rentals);
    }

    public void createRental(Rental rental) {
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());
        rentalRepository.save(rental);
    }

    public RentalDTO getRentalById(Integer id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
        return rentalMapper.toRentalDTO(rental);
    }

    public Rental getRentalEntityById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
    }

    public void updateRental(Rental rental) {
        rental.setUpdatedAt(LocalDateTime.now());
        rentalRepository.save(rental);
    }

}
