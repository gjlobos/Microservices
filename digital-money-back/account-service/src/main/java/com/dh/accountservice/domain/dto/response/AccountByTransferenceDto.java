package com.dh.accountservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountByTransferenceDto {

    private Long id;

    private String cvu;

    private Double availableAmount;

    private String userId;

}
