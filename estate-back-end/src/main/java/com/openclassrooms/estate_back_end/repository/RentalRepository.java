package com.openclassrooms.estate_back_end.repository;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.estate_back_end.model.Rental;

public interface RentalRepository extends CrudRepository<Rental, Integer> {

}
