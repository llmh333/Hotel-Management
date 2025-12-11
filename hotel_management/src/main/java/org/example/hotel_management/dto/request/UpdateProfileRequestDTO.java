package org.example.hotel_management.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequestDTO {

    private String id;

    @Pattern(regexp = "^[\\p{L} .'-]+$")
    private String newFullName;

    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[06789]|8[1-9]|9[0-9])\\d{7}$")
    private String newPhoneNumber;
}
