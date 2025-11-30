package org.example.hotel_management.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.service.IRoomService;
import org.example.hotel_management.service.IServicesService;
import org.example.hotel_management.service.impl.IRoomServiceImpl;
import org.example.hotel_management.service.impl.IServicesServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServiceController {

    @FXML private Button btnCancel;
    @FXML private Button btnConfirmOrder;
    @FXML private Label lblServiceName;
    @FXML private Label lblServicePrice;
    @FXML private Label lblTotal;
    @FXML private SearchableComboBox<RoomResponseDTO> searchableRoom;
    @FXML private Spinner<Integer> spinnerQuantity;

    private Runnable onSuccessCallback;

    private final IRoomService roomService = IRoomServiceImpl.getInstance();
    private final IServicesService serviceService = IServicesServiceImpl.getInstance();
    private static final Logger logger = Logger.getLogger(OrderServiceController.class.getName());

    private ServiceResponseDTO selectedService;

    public void initialize() {
        spinnerQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        spinnerQuantity.valueProperty().addListener((obs, oldVal, newVal) -> calculateTotal());

        // 3. Load danh sách phòng đang có khách
        loadActiveRooms();

        btnCancel.setOnAction(e -> closeDialog());
        btnConfirmOrder.setOnAction(e -> handleOrder());
    }

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    // --- HÀM 1: SETUP DỮ LIỆU ---
    public void setServiceData(ServiceResponseDTO service) {
        this.selectedService = service;
        lblServiceName.setText(service.getName());
        lblServicePrice.setText("$" + service.getPrice());
        calculateTotal(); // Tính tiền lần đầu
    }

    // --- HÀM 2: LOAD PHÒNG & CONVERTER ---
    private void loadActiveRooms() {
        TaskUtil.run(
                null,
                () -> roomService.getOccupiedRooms(), // Query DB: WHERE status = 'OCCUPIED'
                (rooms) -> {
                    searchableRoom.getItems().setAll(rooms);

                    searchableRoom.setConverter(new StringConverter<>() {
                        @Override
                        public String toString(RoomResponseDTO room) {
                            if (room == null) return null;
                            return room.getRoomNumber() + " - " + room.getRoomType();
                        }

                        @Override
                        public RoomResponseDTO fromString(String string) {
                            return null;
                        }
                    });
                }
        );
    }

    private void calculateTotal() {
        if (selectedService != null) {
            int qty = spinnerQuantity.getValue();
            double total = selectedService.getPrice() * qty;
            lblTotal.setText("$ " + String.format("%.2f", total));
        }
    }

    // --- HÀM 3: XỬ LÝ ĐẶT MÓN ---
    private void handleOrder() {
        RoomResponseDTO selectedRoom = searchableRoom.getValue();

        if (selectedRoom == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation", "Please select a room!", null);
            return;
        }

        int quantity = spinnerQuantity.getValue();

        // Kiểm tra tồn kho (Optional)
        if (quantity > selectedService.getQuantity()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Quantity Error", "Not enough stock! Available: " + selectedService.getQuantity(), null);
            return;
        }

        TaskUtil.run(
                btnConfirmOrder,
                () -> serviceService.bookServiceForRoom(selectedRoom.getRoomNumber(), selectedService.getId(), quantity),
                (success) -> {
                    if (success != null) {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully!", null);
                        if (onSuccessCallback != null) {
                            onSuccessCallback.run();
                        }

                        closeDialog();
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to place order.", null);
                    }
                },
                (error) -> logger.log(Level.SEVERE, error.getMessage())
        );
    }

    private void closeDialog() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}