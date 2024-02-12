package com.dh.accountservice.api.service;

import com.dh.accountservice.domain.model.Account;
import com.dh.accountservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.accountservice.domain.dto.response.AccountResponseDto;
import com.dh.accountservice.domain.model.Card;
import com.dh.accountservice.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IAccountService {

    //accounts

    Account findById(Long id) throws ResourceNotFoundException;

    Account createAccount(String userid);

    Optional<AccountResponseDto> update(AccountRequestUpdateAccountInfoDto accountRequestUpdateAccountInfoDto, String userId) throws ResourceNotFoundException;

    //cards

    Card createCard(Card card, Long accountId) throws ResourceNotFoundException;

    Card registerCard(Card card, Long accountId) throws ResourceNotFoundException;

    List<Card> getCardsByAccountId(Long accountId) throws ResourceNotFoundException;

    //transactions
}
