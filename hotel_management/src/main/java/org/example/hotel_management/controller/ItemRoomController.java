package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.LoadFXMLUtil;

import java.util.Map;

public class ItemRoomController {

    @FXML
    private Label fieldRoomNumber;

    @FXML
    private Label fieldRoomType;

    @FXML
    private Label fieldPrice;

    @FXML
    private Label fieldStatus;

    @FXML
    private Button btnBookRoom;

//    @FXML
//    private FontIcon btnPencil;
    private RoomResponseDTO roomResponseDTO;

    public void initialize(){
        btnBookRoom.setOnAction(event -> {
            if (RoomStatus.AVAILABLE.equals(roomResponseDTO.getStatus())) {
                Map<Parent, BookingRoomController> loaderBookingRoom = LoadFXMLUtil.loadFXML(AppConstant.View.bookingRoomPath);

                Parent cardBookingRoom = loaderBookingRoom.keySet().iterator().next();
                BookingRoomController bookingRoomController = loaderBookingRoom.get(cardBookingRoom);
                bookingRoomController.loadData(roomResponseDTO);

                bookingRoomController.setOnBookingSuccessCallback(() -> {
                    updateUIAfterBooking();
                });

                Scene sceneBookingRoom = new Scene(cardBookingRoom);
                Stage stageBookingRoom = new Stage();

                stageBookingRoom.setScene(sceneBookingRoom);
                stageBookingRoom.show();
                stageBookingRoom.setTitle("Booking Room");
                stageBookingRoom.setResizable(false);
                stageBookingRoom.centerOnScreen();
            } else {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "WARNING", "Room is not available", "Please choose another room.");
            }

        });
    }

    public void loadData(RoomResponseDTO responseDTO) {

        fieldRoomNumber.setText(responseDTO.getRoomNumber());
        fieldRoomType.setText(responseDTO.getRoomType().name());
        fieldPrice.setText(String.valueOf(responseDTO.getPricePerHours()));

        String status = responseDTO.getStatus().name();
        fieldStatus.setText(status);

        fieldStatus.getStyleClass().clear();
        fieldStatus.getStyleClass().addAll("label", "lbl-status");

        String cssClass = "status-" + status.toLowerCase();
        fieldStatus.getStyleClass().add(cssClass);
        this.roomResponseDTO = responseDTO;
    }

    private void updateUIAfterBooking() {
        this.roomResponseDTO.setStatus(RoomStatus.OCCUPIED);

        fieldStatus.setText(RoomStatus.AVAILABLE.name());

        fieldStatus.setStyle("-fx-text-fill: #e74c3c;");

        btnBookRoom.setText("Check Out");
    }
}
