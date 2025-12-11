package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.ServiceRequestDTO;
import org.example.hotel_management.dto.response.BookingServiceResponseDTO;
import org.example.hotel_management.dto.response.ServiceBookingResponseDTO;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.enums.ServiceCategory;

import java.util.List;

public interface IServicesService {

    ServiceResponseDTO addService(ServiceRequestDTO serviceRequestDTO);
    ServiceResponseDTO updateService(Long idService, ServiceRequestDTO serviceRequestDTO);
    boolean deleteServiceById(Long id);
    ServiceResponseDTO getServiceById(Long id);
    List<ServiceResponseDTO> getAllServices();
    List<ServiceResponseDTO> findAll(String keyword, ServiceCategory category, int pageNumber, int pageSize);
    BookingServiceResponseDTO bookServiceForRoom(String roomNumber, Long serviceId, int quantity);


    List<ServiceBookingResponseDTO> getServicesByBookingId(Long id);
}
