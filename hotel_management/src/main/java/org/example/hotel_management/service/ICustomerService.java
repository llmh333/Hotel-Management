package org.example.hotel_management.service;

import org.example.hotel_management.dto.response.CustomerResponseDTO;

import java.util.List;

public interface ICustomerService {

    CustomerResponseDTO addCustomer(String fullName, String phoneNumber);
    CustomerResponseDTO updateCustomer(String phoneNumber, String fullName);
    CustomerResponseDTO getCustomerById(Integer id);
    CustomerResponseDTO getCustomerByPhoneNumber(String phoneNumber);
    boolean deleteCustomerByPhoneNumber(String phoneNumber);
    List<CustomerResponseDTO> getAllCustomers();
}
