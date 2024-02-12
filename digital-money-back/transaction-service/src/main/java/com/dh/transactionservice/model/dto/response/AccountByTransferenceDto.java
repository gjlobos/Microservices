package com.dh.transactionservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountByTransferenceDto {

    private String cvu;

    private Double availableAmount;

    private String userId;

}
