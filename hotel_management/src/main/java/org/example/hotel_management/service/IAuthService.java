package org.example.hotel_management.service;

import org.example.hotel_management.dto.LoginRequestDto;
import org.example.hotel_management.dto.RegisterRequestDto;
import org.example.hotel_management.dto.UserResponseDTO;

public interface IAuthService {

    public UserResponseDTO login(LoginRequestDto loginRequestDto);
    public UserResponseDTO register(RegisterRequestDto registerRequestDto);
}
