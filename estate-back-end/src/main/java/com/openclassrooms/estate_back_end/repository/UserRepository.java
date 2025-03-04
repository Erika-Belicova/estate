package com.openclassrooms.estate_back_end.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.estate_back_end.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
