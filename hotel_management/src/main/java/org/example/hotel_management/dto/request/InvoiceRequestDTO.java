package org.example.hotel_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hotel_management.enums.PaymentMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceRequestDTO {

    private PaymentMethod paymentMethod;
    private String customerName;
    private String customerPhoneNumber;
    private String roomNumber;
    private double totalAmount;
    private double totalServiceCharge;
    private double totalRoomCharge;
    private String userId;
}
