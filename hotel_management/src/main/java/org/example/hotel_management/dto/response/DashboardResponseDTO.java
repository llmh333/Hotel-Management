package org.example.hotel_management.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DashboardResponseDTO {

    private Long totalBookings;
    private Long availableRooms;
    private Double monthlyRevenue;

    private List<RecentCheckInDTO> recentCheckIns;

    @Data
    @Builder
    public static class RecentCheckInDTO {
        private String guestName;
        private String roomNumber;
        private String roomType;
        private LocalDateTime checkInDate;
    }
}