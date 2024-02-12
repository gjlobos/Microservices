package com.dh.accountservice.domain.mapper;

import com.dh.accountservice.domain.dto.request.TransferenceRequestDto;
import com.dh.accountservice.domain.dto.request.TransferenceResponseDto;
import com.dh.accountservice.domain.dto.response.AccountByTransferenceDto;
import com.dh.accountservice.domain.dto.response.AccountResponseDto;
import com.dh.accountservice.domain.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    public abstract AccountResponseDto accountToAccountResponseDto(Account account);

    public abstract AccountByTransferenceDto accountToAccountByTransferenceDto(Account account);

    public abstract TransferenceResponseDto transferenceRequestDtoToTransferenceResponseDto(TransferenceRequestDto transferenceRequestDto);

}
