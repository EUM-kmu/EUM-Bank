package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.deal.entity.Deal;
import com.eum.bank.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;

    // 거래 생성
    public void createDeal(Account accountNumber, Long deposit, Long maxPeople, Long postId){
        // 거래 생성
        // 거래상태 a -> WAITING
        Deal deal = Deal.builder()
                .senderAccount(accountNumber)
                .status("WAITING")
                .deposit(deposit)
                .numberOfPeople(maxPeople)
                .postId(postId)
                .build();

        dealRepository.save(deal);
    }
}
