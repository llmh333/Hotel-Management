package org.example.hotel_management.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.kordamp.ikonli.javafx.FontIcon;

public class ItemOccupiedRoomDialogController {
    @FXML private AnchorPane rootPane;
    @FXML private Label lblRoomNumber;
    @FXML private Label lblCustomerName;
    @FXML private FontIcon iconBed;

    private RoomResponseDTO currentRoom;

    public void setData(RoomResponseDTO room) {
        this.currentRoom = room;
        lblRoomNumber.setText(room.getRoomNumber());
        // Giả sử DTO có trường customerName, nếu null thì hiện "Unknown"
    }

    // Hàm đổi màu thẻ khi được chọn (Được gọi từ Controller cha)
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            if (!rootPane.getStyleClass().contains("room-mini-card-selected")) {
                rootPane.getStyleClass().add("room-mini-card-selected");
                // Đổi màu chữ sang trắng cho nổi bật trên nền xanh
                lblRoomNumber.setStyle("-fx-text-fill: white;");
                lblCustomerName.setStyle("-fx-text-fill: #e0e0e0;");
                iconBed.setIconColor(javafx.scene.paint.Color.WHITE);
            }
        } else {
            rootPane.getStyleClass().remove("room-mini-card-selected");
            // Trả về màu cũ
            lblRoomNumber.setStyle(""); // Reset về CSS mặc định
            lblCustomerName.setStyle("");
            iconBed.setIconColor(javafx.scene.paint.Color.valueOf("#1f771c"));
        }
    }

    public RoomResponseDTO getRoom() {
        return currentRoom;
    }
}
