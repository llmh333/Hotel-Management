package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotel_management.dto.request.BookingRoomRequestDTO;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.service.IBookingService;
import org.example.hotel_management.service.ICustomerService;
import org.example.hotel_management.service.impl.IBookingServiceImpl;
import org.example.hotel_management.service.impl.ICustomerServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;
import org.example.hotel_management.util.ValidatorUtil;

import java.time.LocalDate;

public class BookingRoomController {

    @FXML private Label lblRoomNumber;

    @FXML private Label lblRoomType;

    @FXML private Label lblPrice;

    @FXML private TextField fieldSearchPhone;

    @FXML private TextField fieldFullName;

    @FXML private TextField fieldPhone;

    @FXML private DatePicker fieldDateCheckIn;

    @FXML private TextField fieldTimeCheckIn;

    @FXML private DatePicker fieldDateCheckOut;

    @FXML private TextField fieldTimeCheckOut;

    @FXML private Button btnCancel;

    @FXML private Button btnConfirmBooking;

    @FXML private Button btnSearchCustomer;

    private static final IBookingService bookingService = IBookingServiceImpl.getInstance();
    private static final ICustomerService customerService = ICustomerServiceImpl.getInstance();

    private Runnable onBookingSuccessCallback;

    public void initialize() {

        btnCancel.setOnAction(event -> closeDialog());
        btnSearchCustomer.setOnAction(event -> handleSearchCustomer());
        btnConfirmBooking.setOnAction(event -> handleBooking());
    }

    private void handleSearchCustomer() {
        String phoneToSearch = fieldSearchPhone.getText().trim();

        if (phoneToSearch.isEmpty()) {
            return;
        }

        TaskUtil.run(
                null,
                () -> customerService.getCustomerByPhoneNumber(phoneToSearch),
                (customer) -> {
                    if (customer != null) {
                        // TÌM THẤY: Tự động điền thông tin
                        fieldFullName.setText(customer.getFullName());
                        fieldPhone.setText(customer.getPhoneNumber());
                        fieldPhone.setDisable(true);

                    } else {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Not Found", "Customer not found", "Please enter details for new customer.");
                        fieldFullName.clear();
                        fieldPhone.setText(phoneToSearch);
                        fieldPhone.setDisable(false);
                        fieldFullName.setDisable(false);
                        fieldFullName.requestFocus();
                    }
                },
                (error) -> {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Database error", error.getMessage());
                }
        );
    }

    private void handleBooking() {
        LocalDate dateCheckIn = fieldDateCheckIn.getValue();
        LocalDate dateCheckOut = fieldDateCheckOut.getValue();
        String timeCheckIn = fieldTimeCheckIn.getText();
        String timeCheckOut = fieldTimeCheckOut.getText();
        String fullName = fieldFullName.getText();
        String phoneNumber = fieldPhone.getText();

        BookingRoomRequestDTO bookingRequest = BookingRoomRequestDTO.builder()
                .checkIn(dateCheckIn)
                .checkOut(dateCheckOut)
                .timeCheckIn(timeCheckIn)
                .timeCheckOut(timeCheckOut)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .build();

        String errors = ValidatorUtil.validate(bookingRequest);
        if (errors != null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation Error", "Please check input", errors);
            return;
        }

        TaskUtil.run(
                null,
                () -> bookingService.bookRoom(lblRoomNumber.getText(), bookingRequest),
                (isSuccess) -> {
                    if (isSuccess) {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Booking Successful", "Room has been booked!");

                        if (onBookingSuccessCallback != null) {
                            onBookingSuccessCallback.run();
                        }
                        closeDialog();
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Failed", "Booking Failed", "Room might be occupied or system error.");
                    }
                },
                (error) -> {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "System Error", error.getMessage());
                    error.printStackTrace();
                }
        );
    }

    public void loadData(RoomResponseDTO roomResponseDTO) {
        this.lblRoomNumber.setText(roomResponseDTO.getRoomNumber());
        this.lblRoomType.setText(roomResponseDTO.getRoomType().name());
        this.lblPrice.setText(String.valueOf(roomResponseDTO.getPricePerHours()));

        fieldDateCheckIn.setValue(LocalDate.now());
        fieldDateCheckOut.setValue(LocalDate.now().plusDays(1));
        fieldTimeCheckIn.setText("14:00");
        fieldTimeCheckOut.setText("12:00");
    }

    public void setOnBookingSuccessCallback(Runnable callback) {
        this.onBookingSuccessCallback = callback;
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
