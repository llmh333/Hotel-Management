package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.response.BookingResponseDto;
import org.example.hotel_management.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    public static BookingMapper getInstance() {
        return INSTANCE;
    }

    BookingResponseDto toBookingResponseDto(Booking booking);

}
