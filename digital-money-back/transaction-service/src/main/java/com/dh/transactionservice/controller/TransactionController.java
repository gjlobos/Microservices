package com.dh.transactionservice.controller;

import com.dh.transactionservice.exceptions.ResourceNotFoundException;
import com.dh.transactionservice.model.Transaction;
import com.dh.transactionservice.model.dto.request.TransferenceRequestDto;
import com.dh.transactionservice.model.dto.response.DestinationsResponseDto;
import com.dh.transactionservice.model.dto.response.TransactionResponseDto;
import com.dh.transactionservice.model.dto.response.Transference;
import com.dh.transactionservice.service.TransactionService;
import com.dh.transactionservice.shared.handler.ErrorDetails;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dh.transactionservice.shared.handler.ResponseBuilder.*;
import static org.springframework.http.HttpStatus.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.create(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Transaction transaction = transactionService.buscar(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> buscarTodasLasTransacciones() {
        List<Transaction> transactions = transactionService.buscarTodos();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarTransaccion(@PathVariable("id") Long id, @RequestBody Transaction transaction) throws ResourceNotFoundException {
        transaction.setId(id);
        transactionService.actualizar(transaction);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable("id") Long id) throws ResourceNotFoundException {
        transactionService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/last-five-transactions/{accountId}")
    public ResponseEntity<List<Transaction>> getFiveTransactionsByAccount(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getFiveTransactionsByAccount(accountId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/last-five-transactions")
    public ResponseEntity<List<Transaction>> getLastFiveTransactions() {
        List<Transaction> transactions = transactionService.getLastFiveTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/transferences/{accountId}")
    public ResponseEntity<String> transferir(@PathVariable("accountId") Long accountId, @RequestBody Transference transference) throws ResourceNotFoundException {
        return transactionService.transferir(accountId, transference);
    }

    @PostMapping("/accounts/{accountId}/transferences")
    public ResponseEntity<?> sendMoney(@PathVariable("accountId") Long accountId, @RequestBody TransferenceRequestDto transferenceRequestDto) throws ResourceNotFoundException {
        try{
            return responseBuilder(CREATED, transactionService.sendMoney(accountId, transferenceRequestDto));
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(NOT_FOUND, error);
        }
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponseDto> findTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.findTransactionsByAccountId(accountId);
    }

    @GetMapping("/account/{accountId}/transferences")
    public List<String> findDestinations(@PathVariable Long accountId) {
        return transactionService.findDestinations(accountId);
    }

    @GetMapping("/{transactionId}/comprobante")
    public ResponseEntity<?> generatePDF(@PathVariable Long transactionId) {
        try {
            transactionService.generatePDF(transactionId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException | FileNotFoundException |
                 DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}



