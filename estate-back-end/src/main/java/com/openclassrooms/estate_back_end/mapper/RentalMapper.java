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
                                // if the picture already is a list, return it
                                ctx.getSource() instanceof List ? ctx.getSource() : Collections.emptyList() // if the picture is empty, return an empty list
                ).map(source.getPicture(), destination.getPicture());
            }
        });
    }

    public RentalDTO toRentalDTO(Rental rental) {
        return modelMapper.map(rental, RentalDTO.class);
    }

    public Rental toRentalEntity(RentalDTO rentalDTO, User owner) {
        Rental rental = modelMapper.map(rentalDTO, Rental.class);
        if (rentalDTO.getPicture() != null && !rentalDTO.getPicture().isEmpty()) {
            rental.setPicture(rentalDTO.getPicture().get(0)); // first element is the picture
        } else {
            rental.setPicture(null); // picture is empty
        }
        rental.setOwner(owner);
        return rental;
    }

    public List<RentalDTO> toRentalDTOList(List<Rental> rentals) {
        return rentals.stream()
                .map(this::toRentalDTO)
                .collect(Collectors.toList());
    }
}
