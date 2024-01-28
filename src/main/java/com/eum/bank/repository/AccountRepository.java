package com.eum.bank.repository;

import com.eum.bank.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account save(Account account);
    Boolean findByAccountNumber(String accountNumber);
}