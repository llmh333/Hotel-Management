package org.example.hotel_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemResponseDto {

    private String name;
    private String description;
    private int quantity;
    private double price;
    private double total;

}
