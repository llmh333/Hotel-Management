package org.example.hotel_management.service.impl;

import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.dto.LoginRequestDto;
import org.example.hotel_management.dto.RegisterRequestDto;
import org.example.hotel_management.dto.UserResponseDTO;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.enums.Role;
import org.example.hotel_management.mapper.UserMapper;
import org.example.hotel_management.service.IAuthService;
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
    public UserResponseDTO login(LoginRequestDto loginRequestDto) {

        Optional<User> userOptional = userDAO.findByUsername(loginRequestDto.getUsername());
        if (userOptional.isPresent()) {
            if (PasswordUtil.checkPassword(loginRequestDto.getPassword(), userOptional.get().getPassword())) {
                return userMapper.toUserResponseDTO(userOptional.get());
            }
        }
        return null;
    }

    @Override
    public UserResponseDTO register(RegisterRequestDto registerRequestDto) {
        User user = userMapper.toUser(registerRequestDto);

        user.setPassword(PasswordUtil.encode(registerRequestDto.getPassword()));
        user.setRole(Role.STAFF);
        Optional<User> optionalUser = userDAO.add(user);
        return optionalUser.map(userMapper::toUserResponseDTO).orElse(null);

    }
}
