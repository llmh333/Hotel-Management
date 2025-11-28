package org.example.hotel_management.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotel_management.controller.card.RoomCardController;
import org.example.hotel_management.dto.request.BookingRoomRequestDTO;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.service.IBookingService;
import org.example.hotel_management.service.impl.IBookingServiceImpl;
import org.example.hotel_management.util.AlertUtil;
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

    private static final IBookingService bookingService = IBookingServiceImpl.getInstance();

    private Runnable onBookingSuccessCallback;

    public void initialize() {

        btnCancel.setOnAction(event -> {
            Stage currentStage = (Stage) btnCancel.getScene().getWindow();
            currentStage.close();
        });

        btnConfirmBooking.setOnAction(event -> {
            LocalDate dateCheckIn = fieldDateCheckIn.getValue();
            LocalDate dateCheckOut = fieldDateCheckOut.getValue();
            String timeCheckIn = fieldTimeCheckIn.getText();
            String timeCheckOut = fieldTimeCheckOut.getText();
            String fullName = fieldFullName.getText();
            String phoneNumber = fieldPhone.getText();

            BookingRoomRequestDTO bookingRoomRequestDTO = BookingRoomRequestDTO.builder()
                    .checkIn(dateCheckIn)
                    .checkOut(dateCheckOut)
                    .timeCheckIn(timeCheckIn)
                    .timeCheckOut(timeCheckOut)
                    .fullName(fullName)
                    .phoneNumber(phoneNumber)
                    .build();

            String errors = ValidatorUtil.validate(bookingRoomRequestDTO);
            if (errors != null) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Warning", "Please check the information again", errors);
                return;
            }

            if (bookingService.bookRoom(lblRoomNumber.getText(), bookingRoomRequestDTO)) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Booking successful", "Thank you for booking with us!");

                if (onBookingSuccessCallback != null) {
                    onBookingSuccessCallback.run();
                }

                Stage currentStage = (Stage) btnConfirmBooking.getScene().getWindow();
                currentStage.close();
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Booking failed", "Please try again later.");
            }
        });
    }

    public void loadData(RoomResponseDTO roomResponseDTO) {
        this.lblRoomNumber.setText(roomResponseDTO.getRoomNumber());
        this.lblRoomType.setText(roomResponseDTO.getRoomType().name());
        this.lblPrice.setText(String.valueOf(roomResponseDTO.getPricePerHours()));
    }

    public void setOnBookingSuccessCallback(Runnable callback) {
        this.onBookingSuccessCallback = callback;
    }
}
