package com.dh.userservice.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequestUpdateUserInfoDto {

    @Schema(example = "")
    private String dni;

    @Schema(example = "")
    private String firstName;

    @Schema(example = "")
    private String lastName;

    @Schema(example = "")
    private String phone;
}
