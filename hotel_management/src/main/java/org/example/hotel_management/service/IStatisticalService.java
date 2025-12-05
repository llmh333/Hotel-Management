package org.example.hotel_management.service;

import org.example.hotel_management.dto.response.StatisticsResponseDTO;
import java.time.LocalDate;

public interface IStatisticalService {
    StatisticsResponseDTO getRevenueStatistics(LocalDate startDate, LocalDate endDate);
}