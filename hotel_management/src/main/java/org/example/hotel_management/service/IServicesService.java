package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.BookingServiceRequestDTO;
import org.example.hotel_management.dto.request.ServiceRequestDTO;
import org.example.hotel_management.dto.response.BookingServiceResponseDTO;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.entity.Service;
import org.example.hotel_management.enums.ServiceCategory;

import java.util.List;

public interface IServicesService {

    ServiceResponseDTO addService(ServiceRequestDTO serviceRequestDTO);
    ServiceResponseDTO updateService(Integer idService, ServiceRequestDTO serviceRequestDTO);
    boolean deleteServiceById(Integer id);
    ServiceResponseDTO getServiceById(Integer id);
    List<ServiceResponseDTO> getAllServices();
    List<ServiceResponseDTO> findAll(String keyword, ServiceCategory category, int pageNumber, int pageSize);
    BookingServiceResponseDTO bookServiceForRoom(String roomNumber, Integer idService, int quantity);


}
