package org.example.hotel_management.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.dto.response.UserResponseDTO;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.enums.Role;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.AnimationUtil;

import java.io.IOException;
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
    private Button btnLogout;

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

        btnLogout.setOnAction(event -> handleLogout());

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

        btnServices.setOnAction(event -> {
            if (btnServices.isSelected())
                showCard(AppConstant.View.servicesCardPath);
        });

        btnBilling.setOnAction(event -> {
            if (btnBilling.isSelected()) {
                showCard(AppConstant.View.billCardPath);
            }
        });

        btnStatistical.setOnAction(event -> {
            if (btnStatistical.isSelected()) {
                showCard(AppConstant.View.statisticsCardPath);
            }
        });

        btnSetting.setOnAction(event -> {
            if (btnSetting.isSelected()) {
                showCard(AppConstant.View.settingCardPath);
            }
        });

    }

    private void handleLogout() {
        boolean confirm = AlertUtil.showConfirmation("Logout", "Are you sure you want to logout?", null);
        if (!confirm) {
            return;
        }

        UserSessionUtil.getInstance().clearSession();

        try {
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstant.View.authDashboard));
            Parent root = loader.load();

            Stage authStage = new Stage();
            authStage.setTitle("Hotel Management System");
            authStage.setScene(new Scene(root));

            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Logout Successful", null);
            authStage.show();


        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Cannot load Login screen", e.getMessage());
        }
    }

    public void showCard(String path) {
        if (!currentCardPath.equals(path)) {
            try {
                Parent card;

                if (viewCardCache.containsKey(path) && !path.equals(AppConstant.View.billCardPath)) {
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
        if (Role.ADMIN.equals(user.getRole())) {
        } else {
        }
    }

}
