package org.example.hotel_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.hotel_management.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class UserResponseDTO {

    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
