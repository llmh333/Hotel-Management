package org.example.hotel_management.service.impl;

import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.dto.request.RoomRequestDTO;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.mapper.RoomMapper;
import org.example.hotel_management.service.IRoomService;

import java.util.List;
import java.util.stream.Collectors;

public class IRoomServiceImpl implements IRoomService {

    private static final IRoomServiceImpl INSTANCE = new IRoomServiceImpl();
    private final RoomMapper roomMapper = RoomMapper.INSTANCE;
    private final RoomDAO roomDAO = RoomDAO.getInstance();

    public static IRoomServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public RoomResponseDTO addRoom(RoomRequestDTO roomRequestDTO) {
        return null;
    }

    @Override
    public RoomResponseDTO updateRoom(RoomRequestDTO roomRequestDTO) {
        return null;
    }

    @Override
    public boolean deleteRoomByRoomNumber(Integer id) {
        return false;
    }

    @Override
    public List<RoomResponseDTO> getRoomsPagination(int pageNum, int pageSize) {

        List<Room> rooms = roomDAO.getRoomsPagination(pageNum, pageSize);

        return rooms.stream().map(roomMapper::toRoomResponseDTO).collect(Collectors.toList());
    }
}
