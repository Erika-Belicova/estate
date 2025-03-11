package com.openclassrooms.estate_back_end.repository;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.estate_back_end.model.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{

}
