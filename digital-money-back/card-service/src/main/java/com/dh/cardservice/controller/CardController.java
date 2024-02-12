package com.dh.cardservice.controller;

import com.dh.cardservice.exceptions.ResourceNotFoundException;
import com.dh.cardservice.model.Card;
import com.dh.cardservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<Card> crearTarjeta(@RequestBody Card card) {
        Card nuevaTarjeta = cardService.crear(card);
        return new ResponseEntity<>(nuevaTarjeta, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> buscarTarjeta(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Card tarjeta = cardService.buscar(id);
        return new ResponseEntity<>(tarjeta, HttpStatus.OK);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Card>> getCardsByAccount(@PathVariable Long accountId) throws ResourceNotFoundException {
        List<Card> cards = cardService.getCardsByAccount(accountId);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Card>> buscarTodasLasTarjetas() {
        List<Card> tarjetas = cardService.buscarTodos();
        return new ResponseEntity<>(tarjetas, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarTarjeta(@PathVariable("id") Long id, @RequestBody Card card) throws ResourceNotFoundException {
        card.setId(id);
        cardService.actualizar(card);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarjeta(@PathVariable("id") Long id) throws ResourceNotFoundException {
        cardService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
