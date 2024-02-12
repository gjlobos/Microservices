package com.dh.accountservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationsResponseDto {

    private String cvu;

    private String alias;

    private String name;
}
