package com.eum.bank.repository;

import com.eum.bank.domain.account.entity.AccountTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransferHistoryRepository extends JpaRepository<AccountTransferHistory, Long> {
}