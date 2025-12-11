package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.LoginRequestDTO;
import org.example.hotel_management.dto.request.RegisterRequestDTO;
import org.example.hotel_management.dto.response.UserResponseDTO;

public interface IAuthService {

    UserResponseDTO login(LoginRequestDTO loginRequestDto);
    UserResponseDTO register(RegisterRequestDTO registerRequestDto);
}
