package com.dh.accountservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    private Double amount;
    private String dated;
    private String description;
    private String destination;
    private String origin;
    private String type;
    private String category;
}
