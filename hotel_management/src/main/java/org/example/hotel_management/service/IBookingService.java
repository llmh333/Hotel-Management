package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.BookingRoomRequestDTO;
import org.example.hotel_management.dto.response.BookingResponseDto;

public interface IBookingService {

    public boolean bookRoom(String roomNumber, BookingRoomRequestDTO requestDTO);

    BookingResponseDto getBookingByRoom(String roomNumber);

    BookingResponseDto getBookingByRoomOccupied(String roomNumber);
}
