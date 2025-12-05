package org.example.hotel_management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatisticsResponseDTO {
    private Double totalRevenue;
    private Long totalBookings;

    private Double totalRoomRevenue;
    private Double totalServiceRevenue;

    private List<DailyRevenueResponseDTO> dailyRevenues;
}
