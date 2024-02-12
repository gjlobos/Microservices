package com.dh.accountservice.api.client;

import com.dh.accountservice.domain.model.Card;
import com.dh.accountservice.domain.model.Transaction;
import com.dh.accountservice.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="card-service")
public interface CardServiceClient {

    @PostMapping("/cards/")
    ResponseEntity<Card> crearTarjeta(@RequestBody Card card);

    @GetMapping("/cards/account/{accountId}")
    ResponseEntity<List<Card>> getCardsByAccount(@PathVariable Long accountId) throws ResourceNotFoundException;

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> eliminarTarjeta(@PathVariable("id") Long id) throws ResourceNotFoundException;

}
