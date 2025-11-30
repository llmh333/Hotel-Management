package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.request.BookingServiceRequestDTO;
import org.example.hotel_management.dto.response.BookingServiceResponseDTO;
import org.example.hotel_management.entity.BookingService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingServiceMapper {

    BookingServiceMapper INSTANCE = Mappers.getMapper(BookingServiceMapper.class);

    public static BookingServiceMapper getInstance() {
        return INSTANCE;
    }

    BookingServiceResponseDTO toBookingServiceResponseDTO(BookingService bookingService);
    BookingService toBookingService(BookingServiceResponseDTO bookingServiceResponseDTO);
    BookingService toBookingService(BookingServiceRequestDTO bookingServiceRequestDTO);
}
