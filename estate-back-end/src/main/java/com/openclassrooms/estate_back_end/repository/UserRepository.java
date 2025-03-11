package com.openclassrooms.estate_back_end.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.estate_back_end.model.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
