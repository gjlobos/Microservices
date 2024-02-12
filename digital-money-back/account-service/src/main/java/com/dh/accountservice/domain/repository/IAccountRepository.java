package com.dh.accountservice.domain.repository;

import com.dh.accountservice.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long>{

    @Query("SELECT a FROM Account a WHERE user_id = ?1")
    Optional<Account> findByAccountByUserId(String userId);

    @Query("SELECT a FROM Account a WHERE cvu = ?1")
    Optional<Account> findByAccountByCvu(String cvu);

    @Query("SELECT a.alias FROM Account a")
    List<String> findAllAlias();

    Account findAccountById(Long accountId);
    //Account updateAlias(int userId, String alias);
}
