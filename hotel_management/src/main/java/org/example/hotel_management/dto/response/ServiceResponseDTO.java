package org.example.hotel_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hotel_management.enums.ServiceCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDTO {

    private int id;
    private String name;
    private double price;
    private int quantity;
    private ServiceCategory category;
    private String description;
}
