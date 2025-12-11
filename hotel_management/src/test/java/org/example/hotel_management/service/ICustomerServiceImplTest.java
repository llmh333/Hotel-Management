package org.example.hotel_management.service;

import org.example.hotel_management.dao.CustomerDAO;
import org.example.hotel_management.dto.response.CustomerResponseDTO;
import org.example.hotel_management.entity.Customer;
import org.example.hotel_management.mapper.CustomerMapper;
import org.example.hotel_management.service.impl.ICustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class ICustomerServiceImplTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private ICustomerServiceImpl customerService;

    @Test
    public void testGetCustomerByPhoneNumber() {
        String phoneNumber = "033395671";

        Customer mockCustomer = Customer.builder().fullName("nguyen van a").phoneNumber(phoneNumber).build();
        when(customerDAO.findByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(mockCustomer));

        when(customerMapper.toCustomerResponseDTO(mockCustomer))
                .thenReturn(new CustomerResponseDTO(mockCustomer.getId(), mockCustomer.getFullName(), mockCustomer.getPhoneNumber()));

        CustomerResponseDTO customer = customerService.getCustomerByPhoneNumber(phoneNumber);

        System.out.println(customer);

    }
}
