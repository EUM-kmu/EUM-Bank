package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.DealResponseDTO;
import com.eum.bank.common.dto.response.DealResponseDTO.createDeal;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.deal.entity.Deal;
import com.eum.bank.domain.deal.entity.DealReceiver;
import com.eum.bank.exception.InvalidStatusException;
import com.eum.bank.repository.DealReceiverRepository;
import com.eum.bank.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.eum.bank.common.Constant.*;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final AccountService accountService;
    private final DealReceiverRepository dealReceiverRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 거래 생성
     * 1. 계좌번호, 비밀번호 일치여부 확인
     * 2. 가용금액 마이너스
     * 3. 거래 저장
     * 4. 거래ID 반환
     *
     * @param createDeal
     * @return
     */
    @Transactional
    public APIResponse<DealResponseDTO.createDeal> createDeal(DealRequestDTO.CreateDeal createDeal) {

        Account account = accountService.matchAccountPassword(createDeal.getAccountNumber(), createDeal.getPassword());

        Long finalDeposit = this.calculateDeposit(createDeal.getDeposit(), createDeal.getMaxPeople());

        // 가용금액 마이너스
        accountService.changeAvailableBudget(account, finalDeposit, DECREASE);



        // 거래 생성 후 저장 후 거래ID 반환
        Deal deal = dealRepository.save(Deal.initializeDeal(account, createDeal));

        DealResponseDTO.createDeal response = DealResponseDTO.createDeal.builder()
                .dealId(deal.getId())
                .senderAccountNumber(deal.getSenderAccount().getAccountNumber())
                .status(deal.getStatus())
                .deposit(deal.getDeposit())
                .maxPeopleNum(deal.getMaxPeopleNum())
                .postId(deal.getPostId())
                .build();

        return APIResponse.of(SuccessCode.INSERT_SUCCESS, response);
    }

    /**
     * 거래 성사
     * 1. 거래ID로 존재여부 + 거래상태 검증
     * 2. 수신계좌들 검증
     * 3. 최종 예치금 확인해서 차액만큼 가용금액 플러스
     * 4. 수신자 계좌번호 거래에 묶기
     * 5. 거래상태 AFTER_DEAL로 변경
     * 6. 거래ID 반환
     * @param dto
     * @return
     */
    @Transactional
    public APIResponse<DealResponseDTO.createDeal> completeDeal(DealRequestDTO.CompleteDeal dto) {
        // 거래 검증 및 거래 상태 BEFORE_DEAL 인지 검증
        Deal deal = validateDeal(dto.getDealId(), List.of(BEFORE_DEAL));
        List<String> receiverAccountNumbers = List.of(dto.getReceiverAccountNumbers());
        Long realPeopleNum = (long) receiverAccountNumbers.size();

        // 송신계좌 검증 및 잔액 확인
        Account senderAccount =
                accountService.matchAccountPassword(deal.getSenderAccount().getAccountNumber(), dto.getPassword());

//        // 최대 모집인원 - 최종 모집인원 * 예치금 만큼 다시 가용금액 플러스
//        Long diff = calculateDeposit(deal.getDeposit(), deal.getMaxPeopleNum() - realPeopleNum);
//
//        accountService.changeAvailableBudget(senderAccount, diff, INCREASE);

        // 수신자 계좌번호 검증하면서 DealReceiver로 만들어서 저장
        receiverAccountNumbers.forEach(accountNumber -> {
            dealReceiverRepository.save(
                    DealReceiver.initializeDealReceiver(deal, accountService.validateAccount(accountNumber))
            );
        });

        // 거래상태 after_deal 로 변경
        deal.setStatus(AFTER_DEAL);
        deal.setRealPeopleNum(realPeopleNum);
        DealResponseDTO.createDeal response = DealResponseDTO.createDeal.builder()
                .dealId(deal.getId())
                .senderAccountNumber(deal.getSenderAccount().getAccountNumber())
                .status(deal.getStatus())
                .deposit(deal.getDeposit())
                .maxPeopleNum(deal.getMaxPeopleNum())
                .realPeopleNum(deal.getRealPeopleNum())
                .postId(deal.getPostId())
                .build();
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, response);
    }

    /**
     * 거래 수정
     * 1. 거래 rollback
     * 2. 거래 인원수 수정
//     * 3. after_deal 일 경우 dealReceiver 삭제
     * 4. 거래 상태 BEFORE_DEAL 로 변경
     * 5. 거래ID 반환
     * @param dto
     * @return
     */
    @Transactional
    public APIResponse<DealResponseDTO.createDeal> updateDeal(DealRequestDTO.UpdateDeal dto) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = rollbackDeal(dto.getDealId(), dto.getSenderAccountNumber(), dto.getPassword());

        // deal의 상태가 before_deal일 경우만 수정가능
        if (!deal.getStatus().equals(BEFORE_DEAL)) {
            throw new InvalidStatusException("올바르지 않은 거래 상태입니다.");
        }

        //계좌 검증
        Account senderAccount = accountService.matchAccountPassword(dto.getSenderAccountNumber(), dto.getPassword());

        // 예치금 수정 및 송신자 계좌에 가용금액 마이너스
        Long finalDeposit = calculateDeposit(dto.getDeposit(), dto.getNumberOfPeople());

        // 송신자 계좌에 가용금액 마이너스
        accountService.changeAvailableBudget(senderAccount, finalDeposit, DECREASE);

        // 예치금 수정 거래 인원수 수정 거래 상태 before_deal 로 변경
        deal.setDeposit(dto.getDeposit());
        deal.setMaxPeopleNum(dto.getNumberOfPeople());

        DealResponseDTO.createDeal response = DealResponseDTO.createDeal.builder()
                .dealId(deal.getId())
                .senderAccountNumber(deal.getSenderAccount().getAccountNumber())
                .status(deal.getStatus())
                .deposit(deal.getDeposit())
                .maxPeopleNum(deal.getMaxPeopleNum())
                .postId(deal.getPostId())
                .build();
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, response);
    }

    /**
     * 거래 취소
     * 1. 거래 rollback
     * 2. 거래 상태 cancel_deal 로 변경
     * 3. 거래ID 반환
     *
     * @param dto
     * @return
     */
    @Transactional
    public APIResponse<DealResponseDTO.createDeal> cancelDeal(DealRequestDTO.CancelDeal dto) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = rollbackDeal(dto.getDealId(), dto.getSenderAccountNumber(), dto.getPassword());

        // 거래 상태 cancel_deal 로 변경
        deal.setStatus(CANCEL_DEAL);

        DealResponseDTO.createDeal response = DealResponseDTO.createDeal.builder()
                .dealId(deal.getId())
                .senderAccountNumber(deal.getSenderAccount().getAccountNumber())
                .status(deal.getStatus())
                .deposit(deal.getDeposit())
                .maxPeopleNum(deal.getMaxPeopleNum())
                .postId(deal.getPostId())
                .build();
        return APIResponse.of(SuccessCode.DELETE_SUCCESS, response);
    }

    /**
     * 거래 수행
     * 1. 거래 ID 확인
     * 2. 거래 수신계좌들에 자유송금
     * 3. 거래 상태 d로 변경
     * 4. 통합 거래내역리스트 반환
     * @param dto
     * @return
     */
    @Transactional
    public void executeDeal(DealRequestDTO.ExecuteDeal dto) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = this.rollbackDeal(dto.getDealId(), dto.getSenderAccountNumber(), dto.getPassword());

        // 총액으로 가용금액 검증
        accountService.validatePayment(deal.getSenderAccount(), dto.getTotalAmount());

        // 수신계좌들에 각 금액만큼 자유 송금
        dto.getReceiverAndAmounts().forEach(receiver -> {
            AccountResponseDTO.Transfer transfer = AccountResponseDTO.Transfer.builder()
                    .senderAccountNumber(deal.getSenderAccount().getAccountNumber())
                    .receiverAccountNumber(receiver.getReceiverAccountNumber())
                    .amount(receiver.getAmount())
                    .password(dto.getPassword())
                    .transferType(BATCH_TYPE)
                    .build();
            accountService.transfer(transfer);
        });

        // 거래 상태 done_deal 로 변경
        deal.setStatus(DONE_DEAL);

    }

    /**
     * 거래 모집완료 -> 모집중으로 변경
     * 거래를 롤백 시키고 상태를 before_deal로 변경
     * @param dto
     */
    @Transactional
    public void changeDealToBeforeDeal(DealRequestDTO.RollbackDeal dto) {
        Deal deal = rollbackDeal(dto.getDealId(), dto.getSenderAccountNumber(), dto.getPassword());
        if(deal.getStatus().equals(BEFORE_DEAL)){
            throw new InvalidStatusException("이미 모집중인 거래입니다.");
        }
        Account account = accountService.matchAccountPassword(dto.getSenderAccountNumber(), dto.getPassword());

        Long finalDeposit = this.calculateDeposit(deal.getDeposit(), deal.getMaxPeopleNum());

        // 가용금액 마이너스
        accountService.changeAvailableBudget(account, finalDeposit, DECREASE);

        deal.setStatus(BEFORE_DEAL);
    }

    public Long calculateDeposit(Long deposit, Long peopleCnt) {
        return deposit * peopleCnt;
    }

    // 거래ID로 존재여부 + 거래상태 검증
    private Deal validateDeal(Long dealId, List<String> status) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래입니다."));

        if (!status.contains(deal.getStatus())) {
            throw new InvalidStatusException("올바르지 않은 거래 상태입니다.");
        }

        return deal;
    }

    public Deal rollbackDeal(Long dealId, String accountNumber, String password) {
        // 거래ID로 존재여부 + 거래상태 검증
        Deal deal = this.validateDeal(dealId, List.of(BEFORE_DEAL, AFTER_DEAL));

        // 송금자 계좌 검증
        Account senderAccount = accountService.matchAccountPassword(accountNumber, password);

        // 거래 상태가 AFTER_DEAL 일 경우 실제 지원자수 * 예치금만큼 가용금액 플러스
        // 거래 상태가 BEFORE_DEAL 일 경우 최대 지원자수 * 예치금만큼 가용금액 플러스
        if (deal.getStatus().equals(AFTER_DEAL)) {
            accountService.changeAvailableBudget(senderAccount, deal.getDeposit() * deal.getRealPeopleNum(), INCREASE);
            dealReceiverRepository.deleteByDeal(deal);
        }else if (deal.getStatus().equals(BEFORE_DEAL)){
            accountService.changeAvailableBudget(senderAccount, deal.getDeposit() * deal.getMaxPeopleNum(), INCREASE);
        }

        return deal;
    }

}
