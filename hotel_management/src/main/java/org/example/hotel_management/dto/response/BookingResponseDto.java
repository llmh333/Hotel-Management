package org.example.hotel_management.dto.response;

import lombok.*;
import org.example.hotel_management.entity.Customer;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponseDto {

    private Long id;
    private String customerName;
    private String customerPhone;
    private String roomNumber;
    private String roomType;
    private Long roomId;
    private double pricePerHours;
    private String fullName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BigDecimal totalAmount;
    private BookingStatus status;
}
