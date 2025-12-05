package org.example.hotel_management.controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.controller.card.ForgotCardController;

public class AuthDashboardController {

    @FXML
    private ToggleButton btnCardLogin;

    @FXML
    private ToggleButton btnCardRegister;

    @FXML
    private ToggleButton btnCardForgot;

    @FXML
    private StackPane stackPane;

    private String currentCardPath = "";

    @FXML
    public void initialize(){

        switchToLogin();

        if (btnCardLogin.getToggleGroup() != null) {
            btnCardLogin.getToggleGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle == null) {
                    oldToggle.setSelected(true);
                }
            });
        }

        btnCardLogin.setOnAction(event -> {
            if (btnCardLogin.isSelected()) {
                showCard(AppConstant.View.loginCardPath);
            }
        });

        btnCardRegister.setOnAction(event -> {
            if (btnCardRegister.isSelected()) {
                showCard(AppConstant.View.registerCardPath);
            }
        });

        btnCardForgot.setOnAction(event -> {
            if  (btnCardForgot.isSelected()) {
                showCard(AppConstant.View.forgotCardPath);
            }
        });

    }

    private void switchToLogin() {
        if (!btnCardLogin.isSelected()) {
            btnCardLogin.setSelected(true); // Cập nhật trạng thái nút
        }
        showCard(AppConstant.View.loginCardPath);
    }

    public void showCard(String path) {
        if (!currentCardPath.equals(path)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
                Parent card = fxmlLoader.load();
                if (path.equals(AppConstant.View.forgotCardPath)) {
                    ForgotCardController forgotCardController = fxmlLoader.getController();
                    forgotCardController.setOnSuccessCallback(() -> {
                        Platform.runLater(() -> switchToLogin());
                    });
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(card);

                FadeTransition ft = new FadeTransition(Duration.millis(300), card);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
                currentCardPath = path;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
