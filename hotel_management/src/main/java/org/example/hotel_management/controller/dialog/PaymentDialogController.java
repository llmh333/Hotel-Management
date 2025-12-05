package org.example.hotel_management.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hotel_management.dto.request.InvoiceRequestDTO;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.enums.PaymentMethod;
import org.example.hotel_management.service.IInvoiceService;
import org.example.hotel_management.service.impl.IInvoiceServiceImpl;
import org.example.hotel_management.util.AlertUtil;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class PaymentDialogController {

    @FXML private Label lblTotalAmount;

    @FXML private ToggleButton btnCash;
    @FXML private ToggleButton btnTransfer;
    @FXML private ToggleButton btnCard;
    @FXML private ToggleGroup paymentGroup;
    @FXML private VBox boxCash;
    @FXML private VBox boxTransfer;
    @FXML private VBox boxCard;
    @FXML private TextField txtReceived;
    @FXML private Label lblChange;

    @FXML private Button btnConfirmPay;
    @FXML private Button btnCancel;

    private String customerName = "";
    private String customerPhoneNumber = "";
    private String roomNumber = "";
    private double totalRoomCharge;
    private double totalServiceCharge;
    private Long bookingId;

    private double totalAmount = 0.0;
    private Runnable onPaymentSuccess;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    private final IInvoiceService invoiceService = new IInvoiceServiceImpl();

    private final Logger log = Logger.getLogger(PaymentDialogController.class.getSimpleName());
    public void initialize() {
        btnCash.setSelected(true);
        updateView("CASH");

        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
                return;
            }

            if (newVal == btnCash) updateView("CASH");
            else if (newVal == btnTransfer) updateView("TRANSFER");
            else if (newVal == btnCard) updateView("CARD");
        });

        txtReceived.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                txtReceived.setText(oldVal);
                return;
            }
            calculateChange();
        });

        btnConfirmPay.setOnAction(e -> handlePayment());
        btnCancel.setOnAction(e -> closeDialog());
    }

    public void setData(double amount, String customerName, String customerPhoneNumber, double totalRoomCharge, double totalServiceCharge, String roomNumber, Long bookingId) {
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.roomNumber = roomNumber;
        this.totalAmount = amount;
        this.totalRoomCharge = totalRoomCharge;
        this.totalServiceCharge = totalServiceCharge;
        this.bookingId = bookingId;
        txtReceived.setText(String.format("%.2f", amount));
        calculateChange();
    }

    public void setOnPaymentSuccess(Runnable callback) {
        this.onPaymentSuccess = callback;
    }

    private void updateView(String method) {
        boxCash.setVisible(false);
        boxCash.setManaged(false);
        boxTransfer.setVisible(false);
        boxTransfer.setManaged(false);
        boxCard.setVisible(false);
        boxCard.setManaged(false);

        switch (method) {
            case "CASH":
                boxCash.setVisible(true);
                boxCash.setManaged(true);
                btnConfirmPay.setDisable(false);
                calculateChange();
                break;
            case "TRANSFER":
                boxTransfer.setVisible(true);
                boxTransfer.setManaged(true);
                btnConfirmPay.setDisable(false);
                break;
            case "CARD":
                boxCard.setVisible(true);
                boxCard.setManaged(true);
                btnConfirmPay.setDisable(false);
                break;
        }
    }

    private void calculateChange() {
        try {
            if (txtReceived.getText().isEmpty()) {
                lblChange.setText("$ 0.00");
                return;
            }

            double received = Double.parseDouble(txtReceived.getText());
            double change = received - totalAmount;

            if (change < 0) {
                lblChange.setText("Insufficient (" + df.format(Math.abs(change)) + ")");
                lblChange.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
                btnConfirmPay.setDisable(true);
            } else {
                lblChange.setText("$ " + df.format(change));
                lblChange.setStyle("-fx-text-fill: #1f771c; -fx-font-size: 20px; -fx-font-weight: bold;");
                btnConfirmPay.setDisable(false);
            }
        } catch (NumberFormatException e) {
            lblChange.setText("$ 0.00");
        }
    }

    private void handlePayment() {
        boolean confirm = AlertUtil.showConfirmation( "Confirm Payment", "Are you sure you want to pay $" + df.format(totalAmount) + "?", null);
        if (!confirm) return;

        ToggleButton selected = (ToggleButton) paymentGroup.getSelectedToggle();
        String method = selected.getText().toUpperCase(); // CASH, TRANSFER, CREDIT CARD

        PaymentMethod paymentMethod = PaymentMethod.valueOf(method);
        InvoiceRequestDTO invoiceRequestDto = InvoiceRequestDTO.builder()
                .paymentMethod(paymentMethod)
                .totalAmount(totalAmount)
                .customerName(customerName)
                .customerPhoneNumber(customerPhoneNumber)
                .roomNumber(roomNumber)
                .totalRoomCharge(totalRoomCharge)
                .userId(UserSessionUtil.getInstance().getCurrentUser().getId())
                .totalServiceCharge(totalServiceCharge)
                .build();
        log.info("Booking id: " + bookingId);

        invoiceService.addInvoice(invoiceRequestDto, this.bookingId);

        if (onPaymentSuccess != null) {
            onPaymentSuccess.run();
        }
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}