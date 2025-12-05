package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.BookingRoomRequestDTO;
import org.example.hotel_management.dto.response.BookingResponseDTO;

public interface IBookingService {

    public boolean bookRoom(String roomNumber, BookingRoomRequestDTO requestDTO);

    BookingResponseDTO getBookingByRoom(String roomNumber);

    BookingResponseDTO getBookingByRoomOccupied(String roomNumber);
}
