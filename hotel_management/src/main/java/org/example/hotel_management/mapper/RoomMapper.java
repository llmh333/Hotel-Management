package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.request.RoomRequestDTO;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomResponseDTO toRoomResponseDTO(Room room);
    Room toRoom(RoomResponseDTO roomResponseDTO);
    Room toRoom(RoomRequestDTO roomRequestDTO);
}
