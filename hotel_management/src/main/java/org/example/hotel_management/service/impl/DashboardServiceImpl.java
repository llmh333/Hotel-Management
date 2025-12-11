package org.example.hotel_management.service.impl;

import org.example.hotel_management.dao.DashboardDAO;
import org.example.hotel_management.dto.response.DashboardResponseDTO;
import org.example.hotel_management.service.IDashboardService;

public class DashboardServiceImpl implements IDashboardService {

    private static final DashboardServiceImpl INSTANCE = new DashboardServiceImpl();
    private final DashboardDAO dashboardDAO = DashboardDAO.getInstance();

    private DashboardServiceImpl() {}
    public static DashboardServiceImpl getInstance() { return INSTANCE; }

    @Override
    public DashboardResponseDTO getDashboardStatistics() {
        return DashboardResponseDTO.builder()
                .totalBookings(dashboardDAO.countTotalBookings())
                .availableRooms(dashboardDAO.countAvailableRooms())
                .monthlyRevenue(dashboardDAO.getMonthlyRevenue())
                .recentCheckIns(dashboardDAO.getRecentCheckIns())
                .build();
    }
}