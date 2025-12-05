package org.example.hotel_management.service;

public interface IUserService {

    boolean changePassword(String email, String oldPassword, String newPassword);
}
