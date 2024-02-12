package com.dh.transactionservice.api.client;

import com.dh.transactionservice.model.Account;
import com.dh.transactionservice.model.dto.response.AccountByTransferenceDto;
import com.dh.transactionservice.model.dto.response.Transference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/accounts/{accountId}/transferences")
    ResponseEntity<String> realizarTransferencia(@PathVariable("accountId") Long accountId, @RequestBody Transference transferencia);

    @GetMapping("/accounts/{accountId}")
    ResponseEntity<Account> getAccount(@PathVariable("accountId") Long accountId);

    @PutMapping("/accounts/{accountId}")
    void actualizarCuenta(@PathVariable("accountId") Long accountId, @RequestBody Account account);

    @GetMapping("/accounts/user/{userId}/byTransference")
    public Optional<AccountByTransferenceDto> accountTransferenceByUserId(@PathVariable String userId);

    @GetMapping("/accounts/cvu/{cvu}/byTransference")
    public Optional<AccountByTransferenceDto> accountTransferenceByCvu(@PathVariable String cvu);
}
