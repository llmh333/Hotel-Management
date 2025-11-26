package org.example.hotel_management.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.dto.UserResponseDTO;
import org.example.hotel_management.util.AnimationUtil;

import java.util.HashMap;
import java.util.Map;

public class HomeDashboardController {

    @FXML
    private ToggleButton btnDashboard;

    @FXML
    private ToggleButton btnRooms;

    @FXML
    private ToggleButton btnServices;

    @FXML
    private ToggleButton btnBilling;

    @FXML
    private ToggleButton btnStatistical;

    @FXML
    private ToggleButton btnSetting;

    @FXML
    private Label lblUsername;

    @FXML
    private StackPane contentArea;

    private String currentCardPath = "";

    private Map<String, Parent> viewCardCache = new HashMap<>();

    public void initialize() {
        AnimationUtil.applyFor(btnDashboard, btnRooms, btnServices, btnBilling, btnStatistical, btnSetting);

        btnDashboard.setSelected(true);
        showCard(AppConstant.View.dashboardCardPath);

        if (btnDashboard.getToggleGroup() != null) {
            btnDashboard.getToggleGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle == null) {
                    oldToggle.setSelected(true);
                }
            });
        }

        btnDashboard.setOnAction(event -> {
            if (btnDashboard.isSelected()) {
                showCard(AppConstant.View.dashboardCardPath);
            }
        });

        btnRooms.setOnAction(event -> {
            if (btnRooms.isSelected()) {
                showCard(AppConstant.View.roomsCardPath);
            }
        });

    }

    public void showCard(String path) {
        if (!currentCardPath.equals(path)) {
            try {
                Parent card;

                if (viewCardCache.containsKey(path)) {
                    card = viewCardCache.get(path);
                } else {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
                    card = fxmlLoader.load();

                    viewCardCache.put(path, card);
                }

                if (!contentArea.getChildren().contains(card)) {
                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(card);

                    FadeTransition ft = new FadeTransition(Duration.millis(300), card);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.play();
                }


                currentCardPath = path;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUserInfo(UserResponseDTO user) {
        lblUsername.setText(user.getUsername());
        if (user.getRole().equals("ADMIN")) {
        } else {
        }
    }

}
