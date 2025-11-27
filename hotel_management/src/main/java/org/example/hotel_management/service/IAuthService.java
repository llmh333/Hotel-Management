package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.LoginRequestDto;
import org.example.hotel_management.dto.request.RegisterRequestDto;
import org.example.hotel_management.dto.response.UserResponseDTO;

public interface IAuthService {

    UserResponseDTO login(LoginRequestDto loginRequestDto);
    UserResponseDTO register(RegisterRequestDto registerRequestDto);
}
