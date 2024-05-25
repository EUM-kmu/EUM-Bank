package com.eum.bank.repository;

import com.eum.bank.domain.account.entity.AccountTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountTransferHistoryRepository extends JpaRepository<AccountTransferHistory, Long> {
    List<AccountTransferHistory> findByOwnerAccount_AccountNumber(String senderAccountId);
}