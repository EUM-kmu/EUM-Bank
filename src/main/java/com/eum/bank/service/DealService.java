package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.deal.entity.Deal;
import com.eum.bank.domain.deal.entity.DealReceiver;
import com.eum.bank.repository.DealReceiverRepository;
import com.eum.bank.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final AccountService accountService;
    private final DealReceiverRepository dealReceiverRepository;


    // 거래 성사
    //    1. 거래상태 a인지 확인
    //    2. 수신계좌들 검증
    //    3. 최종 예치금 확인해서 차액만큼 가용금액 플러스
    //    4. 수신자 계좌번호 거래에 묶기
    //    5. 거래상태 b로 변경
    //    6. 거래ID 반환
    @Transactional
    public APIResponse<Long> completeDeal(DealRequestDTO.completeDeal dto) {
        // 거래 검증 및 거래 상태 a 인지 검증
        Deal deal = this.validateDeal(dto.getDealId(), List.of("a"));

        List<String> receiverAccountNumbers = List.of(dto.getReceiverAccountNumbers());

        // 송신계좌 검증 및 잔액 확인
        // 최종 예치금 - 기존 거래의 예치금 만큼 송신자 계좌의 가용금액을 마이너스
        Account senderAccount = accountService.validateAccount(deal.getSenderAccount().getAccountNumber());
        Long finalDeposit = dto.getDeposit() - deal.getDeposit();
        if (senderAccount.getAvailableBudget() < finalDeposit) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        senderAccount.setAvailableBudget(senderAccount.getAvailableBudget() - finalDeposit);

        // 수신자 계좌번호 검증하면서 DealReceiver로 만들어서 저장
        for (String receiverAccountNumber : receiverAccountNumbers) {
            dealReceiverRepository.save(
                    DealReceiver.builder()
                            .deal(deal)
                            .receiverAccount(accountService.validateAccount(receiverAccountNumber))
                            .build()
            );
        }

        // 거래상태 b로 변경
        deal.setStatus("b");

        return APIResponse.of(SuccessCode.INSERT_SUCCESS, dealRepository.save(deal).getId());
    }

    // 거래ID로 존재여부 + 거래상태 검증
    private Deal validateDeal(Long dealId, List<String> status) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래입니다."));

        if (!status.contains(deal.getStatus())) {
            throw new IllegalArgumentException("거래 상태가 올바르지 않습니다.");
        }

        return deal;
    }
}
