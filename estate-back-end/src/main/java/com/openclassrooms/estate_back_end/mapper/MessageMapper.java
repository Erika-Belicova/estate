package com.openclassrooms.estate_back_end.mapper;

import com.openclassrooms.estate_back_end.dto.MessageDTO;
import com.openclassrooms.estate_back_end.model.Message;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public MessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // mapping to avoid ModelMapper misinterpreting IDs
        this.modelMapper.addMappings(new PropertyMap<MessageDTO, Message>() {
            @Override
            protected void configure() {
                skip(destination.getMessageId());  // ignore auto-generated messageId
            }
        });
    }

    @Autowired
    private RentalMapper rentalMapper;

    public MessageDTO toMessageDTO(Message message) {
        MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
        messageDTO.setUserId(message.getUser().getUserId());
        messageDTO.setRentalId(message.getRental().getRentalId());
        return messageDTO;
    }

    public Message toMessageEntity(MessageDTO messageDTO, User user, Rental rental) {
        Message message = modelMapper.map(messageDTO, Message.class);
        message.setUser(user);
        message.setRental(rental);
        message.setCreatedAt(java.time.LocalDateTime.now());
        message.setUpdatedAt(java.time.LocalDateTime.now());
        return message;
    }

}