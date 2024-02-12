package com.dh.transactionservice.model.dto.response;

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
