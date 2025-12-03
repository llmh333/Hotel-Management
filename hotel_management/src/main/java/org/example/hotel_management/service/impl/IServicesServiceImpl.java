package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.BookingDAO;
import org.example.hotel_management.dao.BookingServiceDAO;
import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.dao.ServiceDAO;
import org.example.hotel_management.dto.request.BookingServiceRequestDTO;
import org.example.hotel_management.dto.request.ServiceRequestDTO;
import org.example.hotel_management.dto.response.BookingServiceResponseDTO;
import org.example.hotel_management.dto.response.ServiceBookingResponseDto;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.entity.Booking;
import org.example.hotel_management.entity.BookingService;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.entity.Service;
import org.example.hotel_management.enums.ServiceCategory;
import org.example.hotel_management.mapper.BookingServiceMapper;
import org.example.hotel_management.mapper.ServiceMapper;
import org.example.hotel_management.service.IServicesService;
import org.example.hotel_management.util.AlertUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IServicesServiceImpl implements IServicesService {

    private static final IServicesServiceImpl INSTANCE = new IServicesServiceImpl();

    private static final ServiceMapper serviceMapper = ServiceMapper.getInstance();
    private static final BookingServiceMapper bookingServiceMapper = BookingServiceMapper.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final RoomDAO roomDAO = RoomDAO.getInstance();
    private static final BookingDAO bookingDAO = BookingDAO.getInstance();
    private static final BookingServiceDAO bookingServiceDAO = BookingServiceDAO.getInstance();

    public static IServicesServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ServiceResponseDTO addService(ServiceRequestDTO serviceRequestDTO) {

        if (serviceDAO.existsByName(serviceRequestDTO.getName())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Service name already exist", "Please enter another service name");
            return null;
        }

        Service service = serviceMapper.toService(serviceRequestDTO);
        return serviceMapper.toServiceResponseDTO(serviceDAO.add(service).get());

    }

    @Override
    public ServiceResponseDTO updateService(Long idService, ServiceRequestDTO serviceRequestDTO) {

        Optional<Service> serviceOptional = serviceDAO.findById(idService);
        if (serviceOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Service not found", "Please choose another service");
            return null;
        }

        Service service = serviceMapper.toService(serviceRequestDTO);
        return serviceMapper.toServiceResponseDTO(serviceDAO.add(service).get());
    }

    @Override
    public boolean deleteServiceById(Long id) {
        Optional<Service> serviceOptional = serviceDAO.findById(id);
        if (serviceOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Service not found", "Please choose another service");
            return false;
        }

        if (!serviceDAO.delete(serviceOptional.get())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Service is in use", "Please delete all bookings related to this service");
            return false;
        }

        return true;
    }

    @Override
    public ServiceResponseDTO getServiceById(Long id) {
        Optional<Service> serviceOptional = serviceDAO.findById(id);
        if (serviceOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Service not found", "Please choose another service");
            return null;
        }

        return serviceMapper.toServiceResponseDTO(serviceOptional.get());
    }

    @Override
    public List<ServiceResponseDTO> getAllServices() {
        List<Service> services = serviceDAO.findAll();
        return services.stream().map(serviceMapper::toServiceResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDTO> findAll(String keyword, ServiceCategory category, int pageNumber, int pageSize) {
        List<Service> services = serviceDAO.findAll(keyword, category, pageNumber, pageSize);
        return services.isEmpty() ? List.of() : services.stream().map(serviceMapper::toServiceResponseDTO).collect(Collectors.toList());
    }

    @Override
    public BookingServiceResponseDTO bookServiceForRoom(String roomNumber, Long serviceId, int quantity) {

        Optional<Service> serviceOptional = serviceDAO.findById(serviceId);
        if (serviceOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Service not found", "Please choose another service");
            return null;
        }

        Optional<Room> roomOptional = roomDAO.findByRoomNumber(roomNumber);
        if (roomOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Room not found", "Please choose another room");
            return null;
        }

        Optional<Booking> bookingOptional = bookingDAO.findByRoomNumber(roomNumber);
        if (bookingOptional.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "No booking found", "Please book a room first");
            return null;
        }

        Service service = serviceOptional.get();
        service.setQuantity(service.getQuantity() - quantity);
        serviceDAO.update(service);

        Optional<BookingService> bookingServiceOptional = bookingServiceDAO.findByBookingIdAndServiceId(bookingOptional.get().getId(), serviceId);
        if (bookingServiceOptional.isPresent()) {
            BookingService bookingService = bookingServiceOptional.get();
            bookingService.setQuantity(bookingService.getQuantity() + quantity);
            bookingServiceDAO.update(bookingService);
            return bookingServiceMapper.toBookingServiceResponseDTO(bookingService);
        }

        Booking booking = bookingOptional.get();

        BookingService bookingService = BookingService.builder()
                .service(service)
                .booking(booking)
                .quantity(quantity)
                .build();
        BookingServiceResponseDTO bookingServiceResponseDTO = bookingServiceMapper.toBookingServiceResponseDTO(bookingServiceDAO.add(bookingService).get());
        return bookingServiceResponseDTO;
    }

    @Override
    public List<ServiceBookingResponseDto> getServicesByBookingId(Long id) {
        List<ServiceBookingResponseDto> services = serviceDAO.findAllByBookingId(id);
        return services.isEmpty() ? List.of() : services;
    }
}
