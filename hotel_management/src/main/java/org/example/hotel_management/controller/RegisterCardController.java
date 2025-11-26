package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.dto.RegisterRequestDto;
import org.example.hotel_management.dto.UserResponseDTO;
import org.example.hotel_management.service.IAuthService;
import org.example.hotel_management.service.impl.IAuthServiceImpl;
import org.example.hotel_management.util.ValidatorUtil;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.logging.Logger;

public class RegisterCardController {

    @FXML
    private TextField fieldUsername;

    @FXML
    private TextField fieldPassword;

    @FXML
    private TextField fieldEmail;

    @FXML
    private Button btnSignup;

    private static final IAuthService authService = IAuthServiceImpl.getInstance();

    private static final Logger logger = Logger.getLogger(RegisterCardController.class.getName());

    public void initialize(){

        btnSignup.setOnAction(event -> {
            System.out.println("Signup button pressed");
            String username = fieldUsername.getText();
            String password = fieldPassword.getText();
            String email = fieldEmail.getText();

            RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();

            String errorsValidator = ValidatorUtil.validate(registerRequestDto);
            if (errorsValidator != null){
                ValidatorUtil.showErrorValidatorAlert("Warning", errorsValidator);
            } else {
                logger.info("Register request has been sent");

                UserResponseDTO userResponseDTO = authService.register(registerRequestDto);
                if (userResponseDTO != null) {
                    System.out.println("Register successful");
                    Notifications.create()
                            .title("Register successful")
                            .text("Register successful")
                            .graphic(new FontIcon("fas-check-circle"))
                            .hideCloseButton()
                            .position(Pos.BOTTOM_RIGHT)
                            .show();

                    fieldUsername.setText("");
                    fieldPassword.setText("");
                    fieldEmail.setText("");
                }
            }
        });
    }
}
