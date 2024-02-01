package com.eum.bank.service;

import com.eum.bank.common.APIResponse;

import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.common.dto.response.DealResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.TotalTransferHistory;
import com.eum.bank.domain.deal.entity.Deal;
import com.eum.bank.domain.deal.entity.DealReceiver;
import com.eum.bank.repository.DealReceiverRepository;
import com.eum.bank.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final AccountService accountService;
    private final DealReceiverRepository dealReceiverRepository;
    private final PasswordEncoder passwordEncoder;


    // 거래 생성
    public APIResponse<?> createDeal(DealRequestDTO.Create create){
        // 거래 생성
        // 거래상태 a -> WAITING
        String accountNumber = create.getAccountNumber();
        String password = create.getPassword();
        Long deposit = create.getDeposit();
        Long maxPeople = create.getMaxPeople();
        Long postId = create.getPostId();

        Account account = accountService.validateAccount(accountNumber);

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        Deal deal = Deal.builder()
                .senderAccount(account)
                .status("WAITING")
                .deposit(deposit)
                .numberOfPeople(maxPeople)
                .postId(postId)
                .build();

        dealRepository.save(deal);

        DealResponseDTO.Create response = DealResponseDTO.Create.builder()
                .dealId(deal.getId())
                .build();

        return APIResponse.of(SuccessCode.INSERT_SUCCESS, response);
    }
  
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
        deal.setDeposit(dto.getDeposit());

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

    // 거래 수정
    //    1. 거래 ID 확인
    //    2. 송금자 계좌 검증
    //    3. 비밀번호 검증
    //    4. 거래 상태 확인
    //    5. 예치금 수정 및 송신자 계좌에 가용금액 플러스
    //    6. 거래 인원수 수정
    //    7. b일 경우 dealReceiver 삭제
    //    8. 거래 상태 a로 변경
    //    9. 거래ID 반환
    @Transactional
    public APIResponse<Long> updateDeal(DealRequestDTO.updateDeal dto) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = this.validateDeal(dto.getDealId(), List.of("a", "b"));

        // 송금자 계좌 검증
        Account senderAccount = accountService.validateAccount(dto.getSenderAccountNumber());

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), senderAccount.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // 거래 상태 확인
        if (deal.getStatus().equals("b")) {
            // 거래 상태가 b일 경우 dealReceiver 삭제
            dealReceiverRepository.deleteByDeal(deal);
        }

        // 예치금 수정 및 송신자 계좌에 가용금액 마이너스
        Long finalDeposit = dto.getDeposit() - deal.getDeposit();
        if (senderAccount.getAvailableBudget() < finalDeposit) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        senderAccount.setAvailableBudget(senderAccount.getAvailableBudget() - finalDeposit);
        deal.setDeposit(dto.getDeposit());

        // 거래 인원수 수정
        deal.setNumberOfPeople(dto.getNumberOfPeople());

        // 거래 상태 a로 변경
        deal.setStatus("a");

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, dealRepository.save(deal).getId());
    }

    // 거래 취소
    //    1. 거래 ID 확인
    //    2. 송금자 계좌 검증
    //    3. 비밀번호 검증
    //    4. 거래 상태 확인
    //    5. 거래 상태 b일 경우 dealReceiver 삭제
    //    6. 송신자 계좌에 가용금액 플러스
    //    7. 거래 상태 c로 변경
    //    8. 거래ID 반환
    @Transactional
    public APIResponse<Long> cancelDeal(DealRequestDTO.cancelDeal dto) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = this.validateDeal(dto.getDealId(), List.of("a", "b"));

        // 송금자 계좌 검증
        Account senderAccount = accountService.validateAccount(dto.getSenderAccountNumber());

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), senderAccount.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // 거래 상태 확인
        if (deal.getStatus().equals("b")) {
            // 거래 상태가 b일 경우 dealReceiver 삭제
            // 근데 삭제해야 하나?
            dealReceiverRepository.deleteByDeal(deal);
        }

        // 송신자 계좌에 가용금액 플러스
        senderAccount.setAvailableBudget(senderAccount.getAvailableBudget() + deal.getDeposit());

        // 거래 상태 c로 변경
        deal.setStatus("c");

        return APIResponse.of(SuccessCode.DELETE_SUCCESS, dealRepository.save(deal).getId());
    }

    // 거래 수행
    //    1. 거래 ID 확인
    //    2. 수신계좌들에 자유송금
    //    3. 거래 상태 d로 변경
    //    4. 통합 거래내역리스트 반환
    @Transactional
    public APIResponse<List<TotalTransferHistoryResponseDTO.GetTotalTransferHistory>> executeDeal(DealRequestDTO.executeDeal dto) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = this.validateDeal(dto.getDealId(), List.of("b"));

        // 반환할 거래내역 리스트 생성
        List<TotalTransferHistoryResponseDTO.GetTotalTransferHistory> totalTransferHistoryIds = new java.util.ArrayList<>(List.of());

        // 수신계좌들에 자유송금
        List<DealReceiver> dealReceivers = dealReceiverRepository.findAllByDeal(deal);
        for (DealReceiver dealReceiver : dealReceivers) {
            totalTransferHistoryIds.add(
                    accountService.transfer(deal.getSenderAccount().getAccountNumber(), dealReceiver.getReceiverAccount().getAccountNumber(), deal.getDeposit(), dto.getPassword(), "b")
            );
        }

        // 거래 상태 d로 변경
        deal.setStatus("d");

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, totalTransferHistoryIds);
    }
}
