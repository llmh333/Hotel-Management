package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.RegisterRequestDto;
import org.example.hotel_management.dto.UserResponseDTO;
import org.example.hotel_management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public static UserMapper getInstance() {
        return INSTANCE;
    }

    UserResponseDTO toUserResponseDTO(User user);
    User toUser(UserResponseDTO userResponseDTO);

    User toUser(RegisterRequestDto registerRequestDto);

}
