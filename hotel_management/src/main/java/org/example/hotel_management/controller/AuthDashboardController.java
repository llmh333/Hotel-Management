package org.example.hotel_management.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
        showCard("/org/example/hotel_management/fxml/loginCard.fxml");

        if (btnCardLogin.getToggleGroup() != null) {
            btnCardLogin.getToggleGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle == null) {
                    oldToggle.setSelected(true);
                }
            });
        }

        btnCardLogin.setOnAction(event -> {
            if (btnCardLogin.isSelected()) {
                showCard("/org/example/hotel_management/fxml/loginCard.fxml");
            }
        });

        btnCardRegister.setOnAction(event -> {
            if (btnCardRegister.isSelected()) {
                showCard("/org/example/hotel_management/fxml/registerCard.fxml");
            }
        });

        btnCardForgot.setOnAction(event -> {
            if  (btnCardForgot.isSelected()) {
                showCard("/org/example/hotel_management/fxml/forgotCard.fxml");

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
