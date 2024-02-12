package com.dh.accountservice.api.controller;

import com.dh.accountservice.api.client.TransactionServiceClient;
import com.dh.accountservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.accountservice.domain.dto.request.TransferenceRequestDto;
import com.dh.accountservice.domain.dto.response.AccountByTransferenceDto;
import com.dh.accountservice.domain.dto.response.AccountResponseDto;
import com.dh.accountservice.domain.model.Account;
import com.dh.accountservice.domain.model.Card;
import com.dh.accountservice.domain.model.Transaction;
import com.dh.accountservice.domain.model.Transference;
import com.dh.accountservice.exceptions.ResourceNotFoundException;
import com.dh.accountservice.api.service.AccountService;
import com.dh.accountservice.shared.handler.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

import static com.dh.accountservice.shared.handler.ResponseBuilder.*;
import static org.springframework.http.HttpStatus.OK;


@RestController
public class AccountController {

    //@Autowired
    private TransactionServiceClient transactionServiceClient;

    //@Autowired
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable String userId) {
        Account newAccount = accountService.createAccount(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    @PostMapping("/{accountId}/card")
    public ResponseEntity<Card> registerCard(@RequestBody Card card, @PathVariable Long accountId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.registerCard(card, accountId));
    }

    @PostMapping("/{accountId}/create/card")
    public ResponseEntity<Card> createCard(@RequestBody Card card, @PathVariable Long accountId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createCard(card, accountId));
    }

    @GetMapping("/{accountId}/card")
    public ResponseEntity<?> getCardsByAccountId(@PathVariable Long accountId){
        try {
            return responseBuilder(OK, accountService.getCardsByAccountId(accountId));
        } catch (Exception e) {
            HttpStatus httpStatus = determineHttpStatus(e);
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(httpStatus, error);
        }
    }

    @DeleteMapping("/{accountId}/card/{cardId}")
    public ResponseEntity<List<Card>> createAccount(@PathVariable Long accountId, @PathVariable Long cardId) throws ResourceNotFoundException {
        accountService.deleteCard(cardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{accountId}/transaction/last-five-transactions")
        public ResponseEntity<List<Transaction>> getFiveTransactionsByAccount(@PathVariable Long accountId) throws ResourceNotFoundException {
            List<Transaction> transactions = accountService.getFiveTransactionsByAccount(accountId);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountService.getFiveTransactionsByAccount(accountId));
    }

    @GetMapping("/{accountId}/transaction/{id}")
    public ResponseEntity<Transaction> getTransactionByIdByAccount(@PathVariable Long accountId, @PathVariable Long id) throws ResourceNotFoundException {
        Transaction transaction = accountService.getTransactionByIdByAccount(accountId, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> actualizarCuenta(@PathVariable(value = "id") Long accountId,
                                                    @RequestBody Account accountDetails) throws ResourceNotFoundException {
        Account account = accountService.findById(accountId);
        account.setAvailableAmount(accountDetails.getAvailableAmount());
        final Account updatedAccount = accountService.actualizar(account);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Account account = accountService.findById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @PostMapping("/user/{userId}")
    public Optional<AccountResponseDto> updateByUserId(@RequestBody AccountRequestUpdateAccountInfoDto accountRequestUpdateAccountInfoDto, @PathVariable String userId) throws ResourceNotFoundException {
        Optional<AccountResponseDto> accountResponseDto = accountService.update(accountRequestUpdateAccountInfoDto, userId);
        return accountResponseDto;
    }

    @PostMapping("/{accountId}/deposits")
    public ResponseEntity<String> realizarTransferencia(@PathVariable("accountId") Long accountId, @RequestBody Transference transference) throws ResourceNotFoundException {
        return accountService.transferir(accountId, transference);
    }

    @GetMapping("/user/{userId}")
    public Optional<AccountResponseDto> get(@PathVariable String userId){
        return accountService.getAccountByUserId(userId);
    }

    @GetMapping("/user/{userId}/byTransference")
    public Optional<AccountByTransferenceDto> accountTransferenceByUserId(@PathVariable String userId){
        return accountService.accountTransferenceByUserId(userId);
    }

    @GetMapping("/cvu/{cvu}/byTransference")
    public Optional<AccountByTransferenceDto> accountTransferenceByCvu(@PathVariable String cvu){
        return accountService.accountTransferenceByCvu(cvu);
    }

    @PostMapping("/{accountId}/transferences")
    public ResponseEntity<?> sendMoney(@PathVariable("accountId") Long accountId, @RequestBody TransferenceRequestDto transferenceRequestDto) {
        try {
            return responseBuilder(CREATED, accountService.sendMoney(accountId, transferenceRequestDto));
        } catch (Exception e) {
            HttpStatus httpStatus = determineHttpStatus(e);
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(httpStatus, error);
        }
    }

    @GetMapping("/{accountId}/activity")
    public ResponseEntity<?> findTransactionsByAccountId(@PathVariable Long accountId){
        try {
            return responseBuilder(OK, accountService.findTransactionsByAccountId(accountId));
        } catch (Exception e) {
            HttpStatus httpStatus = determineHttpStatus(e);
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(httpStatus, error);
        }
    }

    @GetMapping("/{accountId}/transferences")
    public ResponseEntity<?> findDestinations(@PathVariable Long accountId){
        try {
            return responseBuilder(OK, accountService.findDestinations(accountId));
        } catch (Exception e) {
            HttpStatus httpStatus = determineHttpStatus(e);
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(httpStatus, error);
    }
}

    private HttpStatus determineHttpStatus(Exception e) {
        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @GetMapping("/{accountID}/transactions/{transactionID}/comprobante")
    public ResponseEntity<?> generarComprobante(
            @PathVariable("accountID") Long accountID,
            @PathVariable("transactionID") Long transactionID) throws ResourceNotFoundException {
        return accountService.generarComprobante(accountID, transactionID);
    }

}


