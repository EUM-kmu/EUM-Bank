package com.eum.bank.repository;

import com.eum.bank.domain.account.entity.TotalTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalTransferHistoryRepository extends JpaRepository<TotalTransferHistory, Long> {
}