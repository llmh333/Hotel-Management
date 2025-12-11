package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.request.ServiceRequestDTO;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    public static ServiceMapper getInstance() {
        return INSTANCE;
    }

    ServiceResponseDTO toServiceResponseDTO(Service service);
    Service toService(ServiceResponseDTO serviceResponseDTO);
    Service toService(ServiceRequestDTO serviceRequestDTO);

}
