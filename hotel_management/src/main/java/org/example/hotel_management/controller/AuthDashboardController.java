package org.example.hotel_management.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.hotel_management.constant.AppConstant;

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

        btnCardLogin.setSelected(true);
        showCard(AppConstant.View.loginCardPath);

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

    public void showCard(String path) {
        if (!currentCardPath.equals(path)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
                Parent card = fxmlLoader.load();

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
