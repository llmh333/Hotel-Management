package org.example.hotel_management.service;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.BookingDAO;
import org.example.hotel_management.dao.CustomerDAO;
import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.service.impl.IBookingServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class IBookingServiceImplTest {

    @Mock
    RoomDAO roomDAO;

    @Mock
    BookingDAO bookingDAO;

    @Mock
    CustomerDAO customerDAO;

    @InjectMocks
    IBookingServiceImpl bookingService;

    @Test
    public void testParseDate() {
        String originalTime = null;
        LocalDate date = LocalDate.of(2025,11,15);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime parseTime = LocalTime.parse(originalTime, formatter);
            System.out.println(LocalDateTime.of(date, parseTime));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid time format", "Please enter valid time format");
            throw new RuntimeException(e);
        }
    }
}
