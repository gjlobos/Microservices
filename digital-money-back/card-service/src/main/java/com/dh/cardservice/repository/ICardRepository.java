package com.dh.cardservice.repository;

import com.dh.cardservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c WHERE account_id = ?1")
    List<Card> findByAccountId(Long accountId);
}
