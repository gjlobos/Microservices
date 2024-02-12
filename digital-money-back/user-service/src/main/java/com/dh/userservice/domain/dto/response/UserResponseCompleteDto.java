package com.dh.userservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseCompleteDto {
    
    private String firstName;

    private String lastName;

    private String email;

    private String dni;

    private String phone;

    private String cvu;

    private String alias;

}
