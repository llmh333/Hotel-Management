package org.example.hotel_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.hotel_management.constant.ErrorMessageConstant;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingRoomRequestDTO {

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private LocalDate checkIn;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private LocalDate checkOut;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private String timeCheckIn;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private String timeCheckOut;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private String fullName;

    @NotNull(message = ErrorMessageConstant.Validator.FIELD_CANNOT_BLANK)
    private String phoneNumber;
}
