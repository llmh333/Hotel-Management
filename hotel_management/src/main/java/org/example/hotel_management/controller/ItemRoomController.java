package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.hotel_management.dto.response.RoomResponseDTO;

public class ItemRoomController {

    @FXML
    private Label fieldRoomNumber;

    @FXML
    private Label fieldRoomType;

    @FXML
    private Label fieldPrice;

    @FXML
    private Label
            fieldStatus;

    @FXML
    private Button btnBookRoom;

//    @FXML
//    private FontIcon btnPencil;

    public void initialize(){

    }

    public void loadData(RoomResponseDTO responseDTO) {

        fieldRoomNumber.setText(responseDTO.getRoomNumber());
        fieldRoomType.setText(responseDTO.getRoomType().name());
        fieldPrice.setText(String.valueOf(responseDTO.getPricePerHours()));
        fieldStatus.setText(responseDTO.getStatus().name());
    }
}
