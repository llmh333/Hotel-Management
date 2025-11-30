package org.example.hotel_management.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotel_management.dto.request.ServiceRequestDTO;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.service.IServicesService;
import org.example.hotel_management.service.impl.IServicesServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;
import org.kordamp.ikonli.javafx.FontIcon;

public class ServiceDialogController {

    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private FontIcon headerIcon;
    @FXML private Label lblHeaderTitle;
    @FXML private Spinner<Integer> spinnerQuantity;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;
    @FXML private TextField txtCategory;

    // Service & State
    private final IServicesService serviceService = IServicesServiceImpl.getInstance();
    private ServiceResponseDTO currentService; // Nếu null -> Tạo mới, Khác null -> Update
    private Runnable onSuccessCallback;

    public void initialize() {
        // 1. Cấu hình Spinner (Chỉ số nguyên, Min 0, Max 10000, Mặc định 1)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 1);
        spinnerQuantity.setValueFactory(valueFactory);

        // 2. Validate ô Giá (Chỉ cho nhập số và 1 dấu chấm)
        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                txtPrice.setText(oldValue);
            }
        });

        // 3. Sự kiện đóng
        btnCancel.setOnAction(event -> closeDialog());
        btnSave.setOnAction(event -> handleSave());
    }

    // --- HÀM 1: NHẬN DỮ LIỆU TỪ BÊN NGOÀI ---
    public void setServiceData(ServiceResponseDTO service) {
        this.currentService = service;

        if (service != null) {
            // Chế độ UPDATE
            lblHeaderTitle.setText("Update Service");
            headerIcon.setIconLiteral("fas-edit");

            txtName.setText(service.getName());
            txtPrice.setText(String.valueOf(service.getPrice()));
            spinnerQuantity.getValueFactory().setValue(service.getQuantity());
        } else {
            // Chế độ NEW
            lblHeaderTitle.setText("New Service");
            headerIcon.setIconLiteral("fas-plus-circle");
        }
    }

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    // --- HÀM 2: XỬ LÝ LƯU ---
    private void handleSave() {
        // 1. Check quyền Admin
        if (!UserSessionUtil.getInstance().isAdmin()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,"Access Denied", "Only Admin can perform this action.", null);
            return;
        }

        // 2. Validate Form
        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String quantityText = spinnerQuantity.getValueFactory().getValue().toString();
        String categoryText = txtCategory.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation", "Please fill in all fields.", null);
            return;
        }

        double price = Double.parseDouble(priceText);
        int quantity = spinnerQuantity.getValue();

        ServiceRequestDTO requestDTO = new ServiceRequestDTO(name, price, quantity, categoryText, null);

        // 3. Gọi Service (Chạy ngầm để không đơ UI)
        TaskUtil.run(
                btnSave, // Disable nút save khi đang chạy
                () -> {
                    if (currentService == null) {
                        return serviceService.addService(requestDTO);
                    } else {
                        return serviceService.updateService(Integer.valueOf(currentService.getId()), requestDTO);
                    }
                },
                (success) -> {
                    if (success != null) {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Service saved successfully!", null);
                        if (onSuccessCallback != null) onSuccessCallback.run();
                        closeDialog();
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to save service.", null);
                    }
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", error.getMessage(),null)
        );
    }

    private void closeDialog() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}