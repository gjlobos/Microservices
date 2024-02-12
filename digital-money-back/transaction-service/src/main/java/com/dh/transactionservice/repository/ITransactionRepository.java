package com.dh.transactionservice.repository;

import com.dh.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transaction t WHERE t.accountId = :accountId ORDER BY STR_TO_DATE(t.dated, '%d/%m/%Y') DESC LIMIT 5;", nativeQuery = true)
    List<Transaction> findLastFiveTransactionsByAccountId(Long accountId);

    @Query(value = "SELECT * FROM transaction t ORDER BY STR_TO_DATE(t.dated, '%d/%m/%Y') DESC LIMIT 5;", nativeQuery = true)
    List<Transaction> findLastFiveTransactions();

    @Query(value = "SELECT * FROM transaction t WHERE t.accountID = ?1 ORDER BY STR_TO_DATE(t.dated, '%d/%m/%Y') DESC;", nativeQuery = true)
    List<Transaction> findTransactionsByAccountId(Long accountId);

    @Query(value = "SELECT t.destination FROM transaction t WHERE t.accountID = ?1 AND t.category = 'TRANSFERENCE' ;", nativeQuery = true)
    List<String> findDestinations(Long accountId);

}
