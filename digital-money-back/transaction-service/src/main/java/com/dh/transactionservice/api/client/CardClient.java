package com.dh.transactionservice.api.client;

import com.dh.transactionservice.exceptions.ResourceNotFoundException;
import com.dh.transactionservice.model.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "card-service")
public interface CardClient {

    @GetMapping("/cards")
    ResponseEntity<List<Card>> getCards();

    @GetMapping("/cards/account/{accountId}")
    public ResponseEntity<List<Card>> getCardsByAccount(@PathVariable Long accountId) throws ResourceNotFoundException;
}
