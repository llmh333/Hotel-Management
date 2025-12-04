package org.example.hotel_management.service.impl;

import javafx.scene.control.Alert;
import org.example.hotel_management.dao.BookingDAO;
import org.example.hotel_management.dao.InvoiceDAO;
import org.example.hotel_management.dao.RoomDAO;
import org.example.hotel_management.dto.request.InvoiceRequestDto;
import org.example.hotel_management.entity.*;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.mapper.UserMapper;
import org.example.hotel_management.service.IInvoiceService;
import org.example.hotel_management.util.AlertUtil;

import java.util.Optional;

public class IInvoiceServiceImpl implements IInvoiceService {

    private static final IServicesServiceImpl INSTANCE = new IServicesServiceImpl();
    private final BookingDAO bookingDAO = BookingDAO.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();
    private final InvoiceDAO invoiceDAO = InvoiceDAO.getInstance();
    private final RoomDAO roomDAO = RoomDAO.getInstance();

    public static IServicesServiceImpl getInstance(){
        return INSTANCE;
    }

    @Override
    public boolean addInvoice(InvoiceRequestDto invoiceRequestDto, Long bookingId) {

        User user = userMapper.toUser(UserSessionUtil.getInstance().getCurrentUser());
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(invoiceRequestDto.getTotalAmount());
        invoice.setUser(user);
        invoice.setPaymentMethod(invoiceRequestDto.getPaymentMethod());
        invoice.setTotalRoomCharge(invoiceRequestDto.getTotalRoomCharge());
        invoice.setTotalServiceCharge(invoiceRequestDto.getTotalServiceCharge());

        if (invoiceDAO.add(invoice).isEmpty()) {
            return false;
        }

        Optional<Room> roomOptional = roomDAO.findByRoomNumber(invoiceRequestDto.getRoomNumber());
        if (roomOptional.isEmpty()) {
            return false;
        }
        Room room = roomOptional.get();
        room.setStatus(RoomStatus.AVAILABLE);
        roomDAO.update(room);

        bookingDAO.deleteById(bookingId);
        return true;
    }
}
