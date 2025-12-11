package org.example.hotel_management.dto.request;

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
public class RegisterRequestDTO {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$", message = ErrorMessageConstant.Auth.INVALID_USERNAME)
    private String username;

    @Pattern(regexp = "^\\S{6,}$", message = ErrorMessageConstant.Auth.INVALID_PASSWORD)
    private String password;

    @Email(message = ErrorMessageConstant.Auth.INVALID_EMAIL)
    private String email;

    @Pattern(regexp = "^[\\p{L} .'-]+$", message = ErrorMessageConstant.Auth.INVALID_FULL_NAME)
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[06789]|8[1-9]|9[0-9])\\d{7}$", message = ErrorMessageConstant.Auth.INVALID_PHONE_NUMBER)
    private String phoneNumber;

    private Role role;
}
