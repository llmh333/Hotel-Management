package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.dto.LoginRequestDto;
import org.example.hotel_management.dto.UserResponseDTO;
import org.example.hotel_management.service.IAuthService;
import org.example.hotel_management.service.impl.IAuthServiceImpl;
import org.example.hotel_management.util.ValidatorUtil;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginCardController {

    @FXML
    private TextField fieldUsername;

    @FXML
    private TextField fieldPassword;

    @FXML
    private CheckBox boxRememberMe;

    @FXML
    private Button btnSignIn;

    private static final IAuthService authService = IAuthServiceImpl.getInstance();
    private static final Logger logger = Logger.getLogger(LoginCardController.class.getName());

    public void initialize() {

        btnSignIn.setOnAction(event -> {
            LoginRequestDto requestDto = new LoginRequestDto(fieldUsername.getText(), fieldPassword.getText());

            String errors = ValidatorUtil.validate(requestDto);
            if (errors != null) {
                ValidatorUtil.showErrorValidatorAlert("Warning", errors);
            } else {

                UserResponseDTO userResponseDTO = authService.login(requestDto);
                if (userResponseDTO != null) {
                    Notifications.create()
                            .title("Success")
                            .text("Login Successful!")
                            .position(Pos.BOTTOM_RIGHT)
                            .hideAfter(Duration.seconds(3))
                            .show();
                    try {
                        Stage currentStage = (Stage) btnSignIn.getScene().getWindow();
                        currentStage.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstant.View.homeDashboard));
                        Parent root = loader.load();

                        HomeDashboardController dashboardController = loader.getController();
                        dashboardController.setUserInfo(userResponseDTO);

                        Stage dashboardStage = new Stage();
                        Scene dashboardScene = new Scene(root);

                        dashboardStage.setScene(dashboardScene);

                        dashboardStage.show();


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Notifications.create()
                            .title("Login Failed!")
                            .text("Username or password is incorrect.")
                            .position(Pos.BOTTOM_RIGHT)
                            .hideCloseButton()
                            .show();


                }
            }
        });

    }

}
