package org.example.hotel_management.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.enums.Role;
import org.example.hotel_management.util.DotEnvUtil;
import org.example.hotel_management.util.PasswordUtil;

import java.util.Optional;
import java.util.logging.Logger;

public class DataInitialize {

    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final Logger logger = Logger.getLogger(DataInitialize.class.getName());
    private static final Dotenv dotenv = DotEnvUtil.getDotenv();

    public static void init() {

        logger.info("Initializing data...");
        Optional<User> userOptional = userDAO.findByUsername("admin");
        if (userOptional.isPresent()) {
            logger.info("Data already initialized");
            return;
        }

        String username = dotenv.get("ADMIN_USERNAME");
        String password = PasswordUtil.encode(dotenv.get("ADMIN_PASSWORD"));
        String email = dotenv.get("ADMIN_EMAIL");
        String phoneNumber = dotenv.get("ADMIN_PHONE_NUMBER");
        String fullName = dotenv.get("ADMIN_FULL_NAME");

        User user = User.builder()
                .username(username)
                .password(password)
                .role(Role.ADMIN)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        userDAO.add(user);
        logger.info("Data initialized successfully");
    }
}
