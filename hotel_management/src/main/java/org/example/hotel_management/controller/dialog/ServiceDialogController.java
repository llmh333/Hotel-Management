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

    private final IServicesService serviceService = IServicesServiceImpl.getInstance();
    private ServiceResponseDTO currentService;
    private Runnable onSuccessCallback;

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 1);
        spinnerQuantity.setValueFactory(valueFactory);

        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                txtPrice.setText(oldValue);
            }
        });

        btnCancel.setOnAction(event -> closeDialog());
        btnSave.setOnAction(event -> handleSave());
    }

    public void setServiceData(ServiceResponseDTO service) {
        this.currentService = service;

        if (service != null) {
            lblHeaderTitle.setText("Update Service");
            headerIcon.setIconLiteral("fas-edit");

            txtName.setText(service.getName());
            txtPrice.setText(String.valueOf(service.getPrice()));
            spinnerQuantity.getValueFactory().setValue(service.getQuantity());
        } else {
            lblHeaderTitle.setText("New Service");
            headerIcon.setIconLiteral("fas-plus-circle");
        }
    }

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    private void handleSave() {
        if (!UserSessionUtil.getInstance().isAdmin()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,"Access Denied", "Only Admin can perform this action.", null);
            return;
        }

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

        TaskUtil.run(
                btnSave,
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