package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.OTPCodeDAO;
import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.entity.OTPCode;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.service.IOTPCodeService;
import org.example.hotel_management.util.AlertUtil;

import java.time.LocalDateTime;
import java.util.Optional;

public class IOTPCodeServiceImpl implements IOTPCodeService {

    private static final IOTPCodeServiceImpl INSTANCE = new IOTPCodeServiceImpl();
    public static IOTPCodeServiceImpl getInstance() {
        return INSTANCE;
    }

    private final OTPCodeDAO otpCodeDAO = OTPCodeDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();

    @Override
    public boolean addOTPCode(String otpCode, String email) {

        if (otpCode == null || email == null) {
            return false;
        }

        Optional<User> userOptional = userDAO.findByEmail(email);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        Optional<OTPCode> otpCodeOptional = otpCodeDAO.findByEmail(email);
        if (otpCodeOptional.isPresent()) {
            otpCodeDAO.delete(otpCodeOptional.get());
        }

        OTPCode otpCodeEntity = OTPCode.builder()
                .code(otpCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .userEmail(user)
                .build();

        return otpCodeDAO.add(otpCodeEntity).isPresent();
    }

    @Override
    public boolean checkOTPCode(String otpCode, String email) {

        if (otpCode == null || email == null) return false;
        Optional<OTPCode> otpOptional = otpCodeDAO.findByEmail(email);

        if (otpOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "No OTP request found for this email.", null);
            return false;
        }

        OTPCode entity = otpOptional.get();

        if (!entity.getCode().equals(otpCode)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Wrong OTP code.", null);
            return false;
        }

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "OTP expired. Please resend.", null);
            return false;
        }

        otpCodeDAO.delete(entity);
        return true;
    }

    @Override
    public boolean deleteOTPCode(String email) {
        return false;
    }
}
