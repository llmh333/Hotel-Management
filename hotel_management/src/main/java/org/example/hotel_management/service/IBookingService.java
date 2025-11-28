package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.BookingRoomRequestDTO;

public interface IBookingService {

    public boolean bookRoom(String roomNumber, BookingRoomRequestDTO requestDTO);
}
