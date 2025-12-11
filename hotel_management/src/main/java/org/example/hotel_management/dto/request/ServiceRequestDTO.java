package org.example.hotel_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestDTO {

    private String name;
    private double price;
    private int quantity;
    private String category;
    private String description;
}
