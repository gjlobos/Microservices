package com.dh.userservice.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequestLogoutDto {

    @Schema(example = "")
    private String token;
}
