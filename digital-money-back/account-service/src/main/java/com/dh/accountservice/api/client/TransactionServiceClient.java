package com.dh.accountservice.api.client;

import com.dh.accountservice.domain.dto.response.TransactionResponseDto;
import com.dh.accountservice.domain.model.Transaction;
import com.dh.accountservice.domain.model.Transference;
import com.dh.accountservice.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "transaction-service")
public interface TransactionServiceClient {

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id) throws ResourceNotFoundException;
    
    @GetMapping("/transactions/last-five-transactions/{accountId}")
    ResponseEntity<List<Transaction>> getFiveTransactionsByAccount(@PathVariable Long accountId) throws ResourceNotFoundException;

    @GetMapping("/transactions/account/{accountId}")
    List<TransactionResponseDto> findTransactionsByAccountId(@PathVariable Long accountId);

    @PostMapping("/transactions/transferences/{accountId}")
    ResponseEntity<String> transferir(@PathVariable("accountId") Long accountId, @RequestBody Transference transference);

    @PostMapping("/transactions/")
    ResponseEntity<Transaction> create(@RequestBody Transaction transaction);

    @GetMapping("/transactions/account/{accountId}/transferences")
    public List<String> findDestinations(@PathVariable Long accountId);

    @GetMapping("/transactions/{transactionId}/comprobante")
    ResponseEntity<?> generatePDF (@PathVariable Long transactionId);
}


