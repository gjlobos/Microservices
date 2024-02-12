package com.dh.transactionservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenceResponseDto {

    private String destination;

    private String origin;

    private Double amount;

    private String dated;

    private String description;

}
