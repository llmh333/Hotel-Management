package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.constant.ErrorMessageConstant;
import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.dto.request.LoginRequestDTO;
import org.example.hotel_management.dto.request.RegisterRequestDTO;
import org.example.hotel_management.dto.response.UserResponseDTO;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.enums.Role;
import org.example.hotel_management.mapper.UserMapper;
import org.example.hotel_management.service.IAuthService;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.PasswordUtil;

import java.util.Optional;

public class IAuthServiceImpl implements IAuthService {

    private static final IAuthService INSTANCE = new IAuthServiceImpl();
    private static final UserMapper userMapper = UserMapper.getInstance();
    private static final UserDAO userDAO = UserDAO.getInstance();
    public static IAuthService getInstance() {
        return INSTANCE;
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO loginRequestDto) {

        Optional<User> userOptional = userDAO.findByUsername(loginRequestDto.getUsername());
        if (userOptional.isPresent()) {
            if (PasswordUtil.checkPassword(loginRequestDto.getPassword(), userOptional.get().getPassword())) {
                return userMapper.toUserResponseDTO(userOptional.get());
            }
        }
        return null;
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO registerRequestDto) {
        User user = userMapper.toUser(registerRequestDto);

        if (userDAO.existsByUsername(registerRequestDto.getUsername())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", ErrorMessageConstant.Auth.USERNAME_ALREADY_EXIST, "Please choose another username");
            return null;
        }
        if (userDAO.existsByEmail(registerRequestDto.getEmail())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", ErrorMessageConstant.Auth.EMAIL_ALREADY_EXIST, "Please choose another email");
            return null;
        }
        if (userDAO.existsByPhoneNumber(registerRequestDto.getPhoneNumber())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", ErrorMessageConstant.Auth.PHONE_NUMBER_ALREADY_EXIST, "Please choose another phone number");
            return null;
        }


        user.setPassword(PasswordUtil.encode(registerRequestDto.getPassword()));
        user.setRole(Role.STAFF);
        Optional<User> optionalUser = userDAO.add(user);
        return optionalUser.map(userMapper::toUserResponseDTO).orElse(null);

    }
}
