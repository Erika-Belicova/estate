package com.openclassrooms.estate_back_end.mapper;

import com.openclassrooms.estate_back_end.dto.UserDTO;
import com.openclassrooms.estate_back_end.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User toUserEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

}
