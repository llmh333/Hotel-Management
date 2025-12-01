package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.LoadFXMLUtil;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Map;

public class ItemRoomController {

    @FXML private Label fieldRoomNumber;
    @FXML private Label fieldRoomType;
    @FXML private Label fieldPrice;
    @FXML private Label fieldStatus;
    @FXML private Button btnBookRoom;
    @FXML private FontIcon btnPencil;

    private RoomResponseDTO roomResponseDTO;

    public void initialize() {
        btnBookRoom.setOnAction(event -> handleMainAction());

//        btnPencil.setOnMouseClicked(event -> handleEditRoom());
    }

    public void loadData(RoomResponseDTO responseDTO) {
        this.roomResponseDTO = responseDTO;

        fieldRoomNumber.setText(responseDTO.getRoomNumber());
        fieldRoomType.setText(responseDTO.getRoomType().name());
        fieldPrice.setText(String.valueOf(responseDTO.getPricePerHours()));

        updateStatusUI(responseDTO.getStatus());
        updateButtonState(responseDTO.getStatus());
    }

    private void handleMainAction() {
        if (RoomStatus.AVAILABLE.equals(roomResponseDTO.getStatus())) {
            openBookingDialog();
        } else if (RoomStatus.OCCUPIED.equals(roomResponseDTO.getStatus())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Info", "Check Out Feature", "Coming soon...");
        } else {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Warning", "Room is maintenance", "Cannot perform action.");
        }
    }

    private void openBookingDialog() {
        try {
            Map<Parent, BookingRoomController> loaderMap = LoadFXMLUtil.loadFXML(AppConstant.View.bookingRoomPath);
            Parent root = loaderMap.keySet().iterator().next();
            BookingRoomController controller = loaderMap.get(root);

            controller.loadData(roomResponseDTO);

            controller.setOnBookingSuccessCallback(this::updateUIAfterBooking);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Booking Room");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Cannot open booking dialog", e.getMessage());
        }
    }

    private void handleEditRoom() {
        if (!UserSessionUtil.getInstance().isAdmin()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Access Denied", "Only Admin can edit room details.", null);
            return;
        }
//
//        try {
//            Map<Parent, RoomDialogController> loaderMap = LoadFXMLUtil.loadFXML("/org/example/hotel_management/fxml/dialog/room_dialog.fxml");
//            Parent root = loaderMap.keySet().iterator().next();
//            RoomDialogController controller = loaderMap.get(root);
//
//            controller.setRoomData(this.roomResponseDTO);
//
//            controller.setOnSuccessCallback(updatedRoom -> {
//                this.loadData(updatedRoom);
//            });
//
//            Stage stage = new Stage();
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            Scene scene = new Scene(root);
//            scene.setFill(null);
//            stage.setScene(scene);
//            stage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    private void updateUIAfterBooking() {
        this.roomResponseDTO.setStatus(RoomStatus.OCCUPIED);

        updateStatusUI(RoomStatus.OCCUPIED);
        updateButtonState(RoomStatus.OCCUPIED);
    }

    private void updateStatusUI(RoomStatus status) {
        fieldStatus.setText(status.name());

        fieldStatus.getStyleClass().clear();
        fieldStatus.getStyleClass().addAll("label", "lbl-status"); // Class cơ sở

        String cssClass = "status-" + status.name().toLowerCase();
        fieldStatus.getStyleClass().add(cssClass);
    }

    private void updateButtonState(RoomStatus status) {
        switch (status) {
            case AVAILABLE:
                btnBookRoom.setText("Book Room");
                btnBookRoom.setStyle("");
                btnBookRoom.setDisable(false);
                break;
            case OCCUPIED:
                btnBookRoom.setText("Check Out");
                btnBookRoom.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");
                btnBookRoom.setDisable(false);
                break;
            case MAINTAINED:
                btnBookRoom.setText("Maintenance");
                btnBookRoom.setDisable(true);
                break;
        }
    }
}