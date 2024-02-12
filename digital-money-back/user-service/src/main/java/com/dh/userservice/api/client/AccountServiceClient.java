package com.dh.userservice.api.client;

import com.dh.userservice.Exceptions.ResourceNotFoundException;
import com.dh.userservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.userservice.domain.dto.response.AccountResponseDto;
import com.dh.userservice.domain.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name="account-service")
public interface AccountServiceClient {

    @PostMapping("/accounts/{userId}")
    ResponseEntity<Account> save(@PathVariable String userId);

    @PostMapping("accounts/user/{userId}")
    Optional<AccountResponseDto> updateByUserId(@RequestBody AccountRequestUpdateAccountInfoDto accountRequestUpdateAccountInfoDto, @PathVariable String userId);

    @GetMapping("accounts/user/{userId}")
    Optional<AccountResponseDto> get(@PathVariable String userId) throws ResourceNotFoundException;
}
