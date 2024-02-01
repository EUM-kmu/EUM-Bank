package com.eum.bank.repository;

import com.eum.bank.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account save(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
}