package com.openclassrooms.estate_back_end.service;

import com.openclassrooms.estate_back_end.model.Message;
import com.openclassrooms.estate_back_end.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

}
