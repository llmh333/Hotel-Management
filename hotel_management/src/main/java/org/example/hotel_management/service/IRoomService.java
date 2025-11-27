package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.RoomRequestDTO;
import org.example.hotel_management.dto.response.RoomResponseDTO;

import java.util.List;

public interface IRoomService {

    RoomResponseDTO addRoom(RoomRequestDTO roomRequestDTO);
    RoomResponseDTO updateRoom(RoomRequestDTO roomRequestDTO);
    boolean deleteRoomByRoomNumber(Integer id);
    List<RoomResponseDTO> getRoomsPagination(int pageNum, int pageSize);
}
