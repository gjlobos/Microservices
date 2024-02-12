package com.dh.userservice.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequestLoginDto {

        @Schema(example = "")
        private String email;

        @Schema(example = "")
        private String password;
}
