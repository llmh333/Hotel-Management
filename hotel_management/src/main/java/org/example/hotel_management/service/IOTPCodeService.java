package org.example.hotel_management.service;

public interface IOTPCodeService {

    boolean addOTPCode(String otpCode, String email);
    boolean checkOTPCode(String otpCode, String email);
    boolean deleteOTPCode(String email);
}
