package org.example.hotel_management.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hotel_management.constant.ErrorMessageConstant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequestDTO {

    private String id;

    private String oldPassword;

    @Pattern(regexp = "^\\S{6,}$", message = ErrorMessageConstant.Auth.INVALID_PASSWORD)
    private String newPassword;
}
