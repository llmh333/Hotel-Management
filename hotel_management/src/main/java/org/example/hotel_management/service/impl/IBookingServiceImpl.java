package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.BookingDAO;
import org.example.hotel_management.dao.CustomerDAO;
import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.dto.request.BookingRoomRequestDTO;
import org.example.hotel_management.dto.response.BookingResponseDto;
import org.example.hotel_management.entity.*;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.mapper.BookingMapper;
import org.example.hotel_management.mapper.UserMapper;
import org.example.hotel_management.service.IBookingService;
import org.example.hotel_management.util.AlertUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

public class IBookingServiceImpl implements IBookingService {

    private static final IBookingServiceImpl INSTANCE = new IBookingServiceImpl();
    private final Logger logger = Logger.getLogger(IBookingServiceImpl.class.getName());

    private static final RoomDAO roomDAO = RoomDAO.getInstance();
    private static final BookingDAO bookingDAO = BookingDAO.getInstance();
    private static final CustomerDAO customerDAO = CustomerDAO.getInstance();

    private static final UserSessionUtil userSessionUtil = UserSessionUtil.getInstance();
    private static final UserMapper userMapper = UserMapper.getInstance();
    private static final BookingMapper bookingMapper = BookingMapper.getInstance();
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

    @Override
    public BookingResponseDto getBookingByRoom(String roomNumber) {
        Optional<Room> roomOptional = roomDAO.findByRoomNumber(roomNumber);
        if (roomOptional.isEmpty()) {
            return null;
        }

        Optional<Booking> bookingOptional = bookingDAO.findByRoomNumber(roomNumber);
        if (bookingOptional.isEmpty()) {
            return null;
        }

        Customer customer = bookingOptional.get().getCustomer();
        User user = bookingOptional.get().getUser();
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
                .id(bookingOptional.get().getId())
                .status(bookingOptional.get().getStatus())
                .checkIn(bookingOptional.get().getCheckIn())
                .checkOut(bookingOptional.get().getCheckOut())
                .fullName(user.getFullName())
                .customerName(customer.getFullName())
                .customerPhone(customer.getPhoneNumber())
                .roomNumber(roomOptional.get().getRoomNumber())
                .roomId(roomOptional.get().getId())
                .pricePerHours(roomOptional.get().getPricePerHours())
                .totalAmount(bookingOptional.get().getTotalAmount())
                .build();
        return bookingResponseDto;
    }

    @Override
    public BookingResponseDto getBookingByRoomOccupied(String roomNumber) {
        logger.info("getBookingByRoomOccupied: " + roomNumber);
        Optional<Booking> bookingOptional = bookingDAO.findActiveBookingWithDetails(roomNumber);
        logger.info("getBookingByRoomOccupied: " + bookingOptional);
        if (bookingOptional.isEmpty()) {
            logger.info("getBookingByRoomOccupied: " + "bookingOptional is empty");
            return null;
        }
        Booking booking = bookingOptional.get();

        return BookingResponseDto.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .totalAmount(booking.getTotalAmount())
                .fullName(booking.getUser().getFullName())
                .customerName(booking.getCustomer().getFullName())
                .customerPhone(booking.getCustomer().getPhoneNumber())
                .roomNumber(booking.getRoom().getRoomNumber())
                .roomId(booking.getRoom().getId())
                .pricePerHours(booking.getRoom().getPricePerHours())
                .build();
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
