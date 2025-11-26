package org.example.hotel_management.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.hotel_management.constant.ErrorMessageConstant;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$", message = ErrorMessageConstant.Auth.INVALID_USERNAME)
    private String username;

    @Pattern(regexp = "^\\S{6,}$", message = ErrorMessageConstant.Auth.INVALID_PASSWORD)
    private String password;
}
