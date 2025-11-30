package org.example.hotel_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hotel_management.entity.Booking;
import org.example.hotel_management.entity.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceRequestDTO {

    private Booking booking;
    private Service service;
    private int quantity;
    private double totalPrice;
}
