package org.example.hotel_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.hotel_management.constant.ErrorMessageConstant;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.enums.RoomType;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomRequestDTO {

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private String roomNumber;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    @Min(0)
    private double pricePerHours;

    private String description;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private RoomType roomType;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private RoomStatus status;
}
