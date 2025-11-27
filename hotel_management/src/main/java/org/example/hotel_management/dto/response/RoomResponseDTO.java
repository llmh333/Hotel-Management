package org.example.hotel_management.dto.response;

import lombok.*;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.enums.RoomType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomResponseDTO {

    private Long id;
    private String roomNumber;
    private double pricePerHours;
    private String description;
    private RoomType roomType;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
