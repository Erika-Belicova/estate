package com.openclassrooms.estate_back_end.mapper;

import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentalMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public RentalMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.addMappings(new PropertyMap<Rental, RentalDTO>() {
            @Override
            protected void configure() {
                using(ctx ->
                // if the picture is a string, wrap it in singleton list
                ctx.getSource() instanceof String ? Collections.singletonList((String) ctx.getSource()) :
                        // if the picture is a list, return it - else return an empty list
                        ctx.getSource() instanceof List ? ctx.getSource() : Collections.emptyList()
                ).map(source.getPicture(), destination.getPicture());
            }
        });
    }

    public RentalDTO toRentalDTO(Rental rental) {
        return modelMapper.map(rental, RentalDTO.class);
    }

    public Rental toRentalEntity(RentalDTO rentalDTO, User owner) {
        Rental rental = modelMapper.map(rentalDTO, Rental.class);
        // if the picture list is not empty, set the first element as the rental's picture
        if (rentalDTO.getPicture() != null && !rentalDTO.getPicture().isEmpty()) {
            rental.setPicture(rentalDTO.getPicture().get(0));
        }
        else {
            rental.setPicture(null);
        }
        rental.setOwner(owner);
        return rental;
    }

    // converts a list of rental entities to a list of RentalDTOs
    public List<RentalDTO> toRentalDTOList(List<Rental> rentals) {
        return rentals.stream().map(this::toRentalDTO).collect(Collectors.toList());
    }

}
