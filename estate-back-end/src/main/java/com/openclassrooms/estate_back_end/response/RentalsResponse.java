package com.openclassrooms.estate_back_end.response;

import com.openclassrooms.estate_back_end.dto.RentalDTO;

import java.util.List;

public class RentalsResponse {

    private List<RentalDTO> rentals;

    public RentalsResponse(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }

    public List<RentalDTO> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }

}
