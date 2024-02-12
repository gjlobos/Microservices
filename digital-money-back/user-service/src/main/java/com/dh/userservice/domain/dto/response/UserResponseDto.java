package com.dh.userservice.domain.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String dni;

    private String phone;

}
