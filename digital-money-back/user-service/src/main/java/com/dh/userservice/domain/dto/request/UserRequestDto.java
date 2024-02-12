package com.dh.userservice.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequestDto {

    @Schema(example = "")
    private String dni;

    @Schema(example = "")
    private String email;

    @Schema(example = "")
    private Boolean emailEnabled;

    @Schema(example = "")
    private String firstName;

    @Schema(example = "")
    private String lastName;

    @Schema(example = "")
    private String password;

    @Schema(example = "")
    private String phone;
}
