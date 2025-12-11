package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.CustomerDAO;
import org.example.hotel_management.dto.response.CustomerResponseDTO;
import org.example.hotel_management.entity.Customer;
import org.example.hotel_management.mapper.CustomerMapper;
import org.example.hotel_management.service.ICustomerService;
import org.example.hotel_management.util.AlertUtil;

import java.util.List;
import java.util.Optional;

public class ICustomerServiceImpl implements ICustomerService {

    private static final ICustomerServiceImpl INSTANCE = new ICustomerServiceImpl();
    private static final CustomerMapper customerMapper = CustomerMapper.getInstance();
    private static final CustomerDAO customerDAO = CustomerDAO.getInstance();

    public static ICustomerServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public CustomerResponseDTO addCustomer(String fullName, String phoneNumber) {
        if (fullName == null || phoneNumber == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input", "Please enter valid input");
            return null;
        }
        Optional<Customer> customerOptional = customerDAO.findByPhoneNumber(phoneNumber);
        if (customerOptional.isPresent()) {
            return customerMapper.toCustomerResponseDTO(customerOptional.get());
        }
        Customer customer = Customer.builder().fullName(fullName).phoneNumber(phoneNumber).build();
        return customerMapper.toCustomerResponseDTO(customerDAO.add(customer).get());
    }

    @Override
    public CustomerResponseDTO updateCustomer(String phoneNumber, String fullName) {
        if (fullName == null || phoneNumber == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input", "Please enter valid input");
            return null;
        }

        Optional<Customer> customerOptional = customerDAO.findByPhoneNumber(phoneNumber);
        if (customerOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Customer not found", "Please choose another customer");
            return null;
        }

        Customer customer = customerOptional.get();
        customer.setFullName(fullName);

        customerDAO.update(customer);
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Integer id) {
        return null;
    }

    @Override
    public CustomerResponseDTO getCustomerByPhoneNumber(String phoneNumber) {
        Optional<Customer> customerOptional = customerDAO.findByPhoneNumber(phoneNumber);
        if (customerOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Customer not found", "Please choose another customer");
            return null;
        }
        Customer customer = customerOptional.get();
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public boolean deleteCustomerByPhoneNumber(String phoneNumber) {
        return false;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return List.of();
    }
}
