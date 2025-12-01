package org.example.hotel_management.mapper;

import org.example.hotel_management.dto.response.CustomerResponseDTO;
import org.example.hotel_management.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    public static CustomerMapper getInstance() {
        return INSTANCE;
    }

    CustomerResponseDTO toCustomerResponseDTO(Customer customer);
}
