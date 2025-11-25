package org.example.hotel_management.controller.card;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import org.example.hotel_management.constant.AppConstant;

import java.io.IOException;

public class RoomCardController {

    @FXML
    private FlowPane containerRooms;

    public void initialize() throws IOException {

        for(int i = 0; i < 10; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(AppConstant.View.itemRoomsPath));

            Parent cardItem = loader.load();

            containerRooms.getChildren().add(cardItem);
        }
    }
}
