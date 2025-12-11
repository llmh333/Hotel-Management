package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.request.RegisterRequestDTO;
import org.example.hotel_management.dto.response.UserResponseDTO;
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

    User toUser(RegisterRequestDTO registerRequestDto);

}
