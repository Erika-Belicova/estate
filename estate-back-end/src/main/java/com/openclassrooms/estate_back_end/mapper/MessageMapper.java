package com.openclassrooms.estate_back_end.mapper;

import com.openclassrooms.estate_back_end.dto.MessageDTO;
import com.openclassrooms.estate_back_end.dto.RentalDTO;
import com.openclassrooms.estate_back_end.model.Message;
import com.openclassrooms.estate_back_end.model.Rental;
import com.openclassrooms.estate_back_end.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public MessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    private RentalMapper rentalMapper;

    public MessageDTO toMessageDTO(Message message) {
        MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
        messageDTO.setUserId(message.getUser().getUserId());
        messageDTO.setRentalId(message.getRental().getRentalId());
        return messageDTO;
    }

    public Message toMessageEntity(MessageDTO messageDTO, User user, RentalDTO rentalDTO) {
        Message message = modelMapper.map(messageDTO, Message.class);
        Rental rental = rentalMapper.toRentalEntity(rentalDTO, user);
        message.setUser(user);
        message.setRental(rental);
        return message;
    }

}