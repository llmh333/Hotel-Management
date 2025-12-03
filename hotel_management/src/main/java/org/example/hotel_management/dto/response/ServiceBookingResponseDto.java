package org.example.hotel_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceBookingResponseDto {

    private String name;
    private String description;
    private int quantityOrdered;
    private double price;
    private double totalPrice;
}
