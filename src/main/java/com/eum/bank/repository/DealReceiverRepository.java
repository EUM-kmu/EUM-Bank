package com.eum.bank.repository;

import com.eum.bank.domain.deal.entity.Deal;
import com.eum.bank.domain.deal.entity.DealReceiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealReceiverRepository extends JpaRepository<DealReceiver, Long> {
    void deleteByDeal(Deal deal);
}