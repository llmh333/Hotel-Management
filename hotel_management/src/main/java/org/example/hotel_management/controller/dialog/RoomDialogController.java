package org.example.hotel_management.controller.dialog;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.dto.request.RoomRequestDTO; // Cần tạo DTO này cho request
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.enums.RoomType;
import org.example.hotel_management.service.IRoomService;
import org.example.hotel_management.service.impl.IRoomServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

public class RoomDialogController {

    @FXML private Label lblHeaderTitle;
    @FXML private FontIcon headerIcon;
    @FXML private TextField txtRoomNumber;
    @FXML private TextField txtPrice;
    @FXML private ComboBox<RoomType> comboType;
    @FXML private ComboBox<RoomStatus> comboStatus;
    @FXML private Button btnDelete;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private final IRoomService roomService = IRoomServiceImpl.getInstance();
    private RoomResponseDTO currentRoom;
    private Consumer<RoomResponseDTO> onSuccessCallback;

    public void initialize() {
        // 1. Load dữ liệu vào ComboBox
        comboType.setItems(FXCollections.observableArrayList(RoomType.values()));
        comboStatus.setItems(FXCollections.observableArrayList(RoomStatus.values()));

        // 2. Validate Price (Chỉ nhập số)
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                txtPrice.setText(oldVal);
            }
        });

        // 3. Sự kiện nút
        btnCancel.setOnAction(e -> closeDialog());
        btnSave.setOnAction(e -> handleSave());
        btnDelete.setOnAction(e -> handleDelete());
    }

    // --- HÀM SET DATA ---
    public void setRoomData(RoomResponseDTO room) {
        this.currentRoom = room;

        if (room != null) {
            // Chế độ UPDATE
            lblHeaderTitle.setText("Edit Room: " + room.getRoomNumber());
            txtRoomNumber.setText(room.getRoomNumber());
            txtPrice.setText(String.valueOf(room.getPricePerHours())); // Hoặc PricePerNight tùy logic
            comboType.setValue(room.getRoomType());
            comboStatus.setValue(room.getStatus());

            btnDelete.setVisible(true);

            txtRoomNumber.setDisable(true);
        } else {
            lblHeaderTitle.setText("Add New Room");
            btnDelete.setVisible(false);
            txtRoomNumber.setDisable(false);
            comboStatus.setValue(RoomStatus.AVAILABLE);
        }
    }

    public void setOnSuccessCallback(Consumer<RoomResponseDTO> callback) {
        this.onSuccessCallback = callback;
    }

    // --- XỬ LÝ LƯU ---
    private void handleSave() {
        // Validate cơ bản
        if (txtPrice.getText().isEmpty() || comboType.getValue() == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,"Validation", "Please fill all fields.", null);
            return;
        }

        double price = Double.parseDouble(txtPrice.getText());
        RoomType type = comboType.getValue();
        RoomStatus status = comboStatus.getValue();

        RoomRequestDTO request = new RoomRequestDTO();
        request.setRoomNumber(txtRoomNumber.getText());
        request.setPricePerHours(price);
        request.setRoomType(type);
        request.setStatus(status);

        TaskUtil.run(
                btnSave,
                () -> {
                    if (currentRoom != null) return roomService.updateRoom(request);
                    else return roomService.addRoom(request);
                },
                (updatedRoom) -> {
                    if (updatedRoom != null) {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Room saved successfully!", null);
                        if (onSuccessCallback != null) onSuccessCallback.accept(updatedRoom);
                        closeDialog();
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to save room.", null);
                    }
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", error.getMessage(), null)
        );
    }

    // --- XỬ LÝ XÓA ---
    private void handleDelete() {
        if (AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this room?",null)) {
            TaskUtil.run(
                    btnDelete,
                    () -> roomService.deleteRoomByRoomNumber(currentRoom.getRoomNumber()),
                    (success) -> {
                        if (success) {
                            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Deleted", "Room deleted.", null);
                            // Gọi callback với null để báo hiệu đã xóa -> Parent nên reload list
                            if (onSuccessCallback != null) onSuccessCallback.accept(null);
                            closeDialog();
                        } else {
                            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not delete room.", null);
                        }
                    },
                    (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", error.getMessage(), null)
            );
        }
    }

    private void closeDialog() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}