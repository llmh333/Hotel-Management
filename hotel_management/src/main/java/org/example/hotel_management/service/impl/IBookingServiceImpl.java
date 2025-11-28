package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.BookingDAO;
import org.example.hotel_management.dao.CustomerDAO;
import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.dto.request.BookingRoomRequestDTO;
import org.example.hotel_management.entity.Booking;
import org.example.hotel_management.entity.Customer;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.mapper.UserMapper;
import org.example.hotel_management.service.IBookingService;
import org.example.hotel_management.util.AlertUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class IBookingServiceImpl implements IBookingService {

    private static final IBookingServiceImpl INSTANCE = new IBookingServiceImpl();

    private static final RoomDAO roomDAO = RoomDAO.getInstance();
    private static final BookingDAO bookingDAO = BookingDAO.getInstance();
    private static final CustomerDAO customerDAO = CustomerDAO.getInstance();

    private static final UserSessionUtil userSessionUtil = UserSessionUtil.getInstance();
    private static final UserMapper userMapper = UserMapper.getInstance();
    public static IBookingServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean bookRoom(String roomNumber, BookingRoomRequestDTO requestDTO) {
        Optional<Customer> customerOptional = customerDAO.findByPhoneNumber(requestDTO.getPhoneNumber());
        if (customerOptional.isEmpty()) {
            customerOptional = customerDAO.add(Customer.builder().fullName(requestDTO.getFullName()).phoneNumber(requestDTO.getPhoneNumber()).build());
        }
        Optional<Room> roomOptional = roomDAO.findByRoomNumber(roomNumber);
        if (roomOptional.isEmpty()) {
            return false;
        }

        Room room = roomOptional.get();
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room is not available", "Please choose another room");
            return false;
        }

        LocalDateTime checkIn = parseDate(requestDTO.getCheckIn(), requestDTO.getTimeCheckIn());
        LocalDateTime checkOut = parseDate(requestDTO.getCheckOut(), requestDTO.getTimeCheckOut());
        if (checkOut != null && checkIn.isAfter(checkOut)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid date format", "Please enter valid date format");
            return false;
        }
        Booking booking = Booking.builder()
                .room(room)
                .customer(customerOptional.get())
                .checkIn(checkIn)
                .checkOut(checkOut)
                .user(userMapper.toUser(userSessionUtil.getCurrentUser()))
                .build();
        bookingDAO.add(booking);

        room.setStatus(RoomStatus.OCCUPIED);
        roomDAO.update(room);
        return true;
    }

    private LocalDateTime parseDate(LocalDate date, String originalTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime parseTime = LocalTime.parse(originalTime, formatter);
            return LocalDateTime.of(date, parseTime);
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid time format", "Please enter valid time format");
            throw e;
        }
    }
}
