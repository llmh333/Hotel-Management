package org.example.hotel_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.hotel_management.constant.ErrorMessageConstant;
import org.example.hotel_management.enums.Role;

@Setter
@Getter
@ToString
@Builder
public class RegisterRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$", message = ErrorMessageConstant.Auth.INVALID_USERNAME)
    private String username;

    @Pattern(regexp = "^\\S{6,}$", message = ErrorMessageConstant.Auth.INVALID_PASSWORD)
    private String password;

    @Email(message = ErrorMessageConstant.Auth.INVALID_EMAIL)
    private String email;

    private Role role;
}
