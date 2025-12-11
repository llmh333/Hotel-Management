package org.example.hotel_management.service.impl;

import org.example.hotel_management.dao.StatisticalDAO;
import org.example.hotel_management.dto.response.DailyRevenueResponseDTO;
import org.example.hotel_management.dto.response.StatisticsResponseDTO;
import org.example.hotel_management.service.IStatisticalService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class IStatisticalServiceImpl implements IStatisticalService {

    private static final IStatisticalServiceImpl INSTANCE = new IStatisticalServiceImpl();
    private final StatisticalDAO statisticalDAO = StatisticalDAO.getInstance();

    private IStatisticalServiceImpl() {}
    public static IStatisticalServiceImpl getInstance() { return INSTANCE; }

    @Override
    public StatisticsResponseDTO getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        // 1. Chuyển đổi ngày
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // 2. Lấy số liệu tổng quan (1 lần gọi DB)
        Object[] stats = statisticalDAO.getInvoiceStats(startDateTime, endDateTime);

        Long totalBookings = (Long) stats[0];
        Double totalRevenue = stats[1] != null ? (Double) stats[1] : 0.0;
        Double totalRoomRevenue = stats[2] != null ? (Double) stats[2] : 0.0;
        Double totalServiceRevenue = stats[3] != null ? (Double) stats[3] : 0.0;

        // 3. Lấy dữ liệu biểu đồ
        List<DailyRevenueResponseDTO> dailyRevenues = statisticalDAO.getDailyRevenueStats(startDateTime, endDateTime);

        // 4. Đóng gói DTO
        return StatisticsResponseDTO.builder()
                .totalRevenue(totalRevenue)
                .totalBookings(totalBookings)
                .totalRoomRevenue(totalRoomRevenue)
                .totalServiceRevenue(totalServiceRevenue)
                .dailyRevenues(dailyRevenues)
                .build();
    }
}
