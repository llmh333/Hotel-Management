package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.dto.request.RoomRequestDTO;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.mapper.RoomMapper;
import org.example.hotel_management.service.IRoomService;
import org.example.hotel_management.util.AlertUtil;

import java.util.List;
import java.util.Optional;
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
        Optional<Room> roomOptional = roomDAO.findByRoomNumber(roomRequestDTO.getRoomNumber());
        if (roomOptional.isPresent()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room number already exist", "Please enter another room number");
            return null;
        }

        Room room = roomMapper.toRoom(roomRequestDTO);

        Optional<Room> roomResponseAdd = roomDAO.add(room);
        if (roomResponseAdd.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Something wrong went add room, please try again", null);
            return null;
        }
        return roomMapper.toRoomResponseDTO(roomResponseAdd.get());
    }

    @Override
    public RoomResponseDTO updateRoom(RoomRequestDTO roomRequestDTO) {
        Optional<Room> roomOptional = roomDAO.findByRoomNumber(roomRequestDTO.getRoomNumber());
        if (roomOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room not found", "Please choose another room");
            return null;
        }

        Room room = roomOptional.get();
        room.setRoomNumber(roomRequestDTO.getRoomNumber());
        room.setRoomType(roomRequestDTO.getRoomType());
        room.setPricePerHours(roomRequestDTO.getPricePerHours());
        room.setStatus(roomRequestDTO.getStatus());

        Optional<Room> roomResponseUpdate = roomDAO.update(room);
        if (roomResponseUpdate.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room is in use", "Please complete all bookings related to this room");
            return null;
        }
        return roomMapper.toRoomResponseDTO(roomResponseUpdate.get());
    }

    @Override
    public boolean deleteRoomByRoomNumber(String roomNumber) {

        Optional<Room> roomOptional = roomDAO.findByRoomNumber(roomNumber);
        if (roomOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room not found", "Please choose another room");
            return false;
        }

        if (!roomDAO.delete(roomOptional.get())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room is in use", "Please complete all bookings related to this room");
            return false;
        }
        return true;
    }

    @Override
    public List<RoomResponseDTO> getRoomsPagination(String keywords, int pageNum, int pageSize) {

        List<Room> rooms = roomDAO.getRoomsPagination(keywords, pageNum, pageSize);

        return rooms.stream().map(roomMapper::toRoomResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDTO> getOccupiedRooms() {
        List<Room> rooms = roomDAO.getOccupiedRooms();
        return rooms.isEmpty() ? List.of() : rooms.stream().map(roomMapper::toRoomResponseDTO).collect(Collectors.toList());
    }
}
