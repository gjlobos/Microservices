package com.dh.userservice.domain.dto.response;

import lombok.Data;

@Data
public class UserResponseLogoutDto {

    private String message;

    public UserResponseLogoutDto(String message) {
        this.message = message;
    }

    public UserResponseLogoutDto() {
    }

}
