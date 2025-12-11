package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.ChangePasswordRequestDTO;
import org.example.hotel_management.dto.request.UpdateProfileRequestDTO;
import org.example.hotel_management.dto.response.UserResponseDTO;

public interface IUserService {

    boolean changePassword(String email, String oldPassword, String newPassword);

    boolean changePassword(ChangePasswordRequestDTO request);
    UserResponseDTO updateProfile(UpdateProfileRequestDTO request);
}
