package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.ErrorResponse;
import com.eum.bank.common.dto.request.AccountTransferHistoryRequestDTO;
import com.eum.bank.common.dto.request.TotalTransferHistoryRequestDTO;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.ErrorCode;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import com.eum.bank.domain.account.entity.TotalTransferHistory;
import com.eum.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Random;

import static com.eum.bank.common.Constant.ACCOUNT_NUMBER_LENGTH;
import static com.eum.bank.common.Constant.FREE_TYPE;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTransferHistoryService accountTransferHistoryService;
    private final TotalTransferHistoryService totalTransferHistoryService;


    public APIResponse<?> createAccount(String password) {

        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        }while (
                !validateAccountNumber(accountNumber)
        );

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .password(passwordEncoder.encode(password))
                .totalBudget(0L)
                .availableBudget(0L)
                .isBlocked(false)
                .build();

        accountRepository.save(account);

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, AccountResponseDTO.Create.builder()
                .accountNumber(account.getAccountNumber())
                .build());
    }

    public String generateAccountNumber() {
        Random random = new Random();
        StringBuilder uniqueNumber = new StringBuilder();

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10);
            uniqueNumber.append(digit);
        }

        return uniqueNumber.toString();
    }

    public Boolean validateAccountNumber(String accountNumber) {
        if (accountNumber.length() != ACCOUNT_NUMBER_LENGTH) {
            return false;
        }

        // 계좌번호 중복 검증
        //  - 계좌번호가 중복되면 false
        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            return false;
        }

        return true;
    }

    // 계좌 검증 (계좌 존재여부 + 블락 여부)
    public Account validateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new IllegalArgumentException("Invalid account number : " + accountNumber));
        if(account.getIsBlocked()){
            throw new IllegalArgumentException("Blocked account : " + accountNumber);
        }
        return account;
    }

    // 계좌번호와 비밀번호로 계좌 조회
    public APIResponse<AccountResponseDTO.AccountInfo> getAccount(String accountNumber, String password) {
        Account account = this.validateAccount(accountNumber);

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, AccountResponseDTO.AccountInfo.builder()
                .accountNumber(account.getAccountNumber())
                .totalBudget(account.getTotalBudget())
                .availableBudget(account.getAvailableBudget())
                .build());
    }

    // 자유송금
    //  1. 송금자 계좌, 수신자 계좌 상태 검증
    //  2. 송금자 잔액 확인
    //  3. 송금자 전체금액, 가용금액 마이너스
    //  4. 수신자 전체금액, 가용금액 플러스
    //  5. 통합 거래내역 생성, 각 계좌 거래내역 생성
    @Transactional
    public TotalTransferHistoryResponseDTO.GetTotalTransferHistory transfer(String senderAccountNumber, String receiverAccountNumber, Long amount, String password, String transferType) {
        Account senderAccount = this.validateAccount(senderAccountNumber);
        Account receiverAccount = this.validateAccount(receiverAccountNumber);

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, senderAccount.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // 송금자 잔액 검증
        if (senderAccount.getAvailableBudget() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // 송금자 잔액 마이너스
        if(transferType.equals(FREE_TYPE)){
            senderAccount.setAvailableBudget(senderAccount.getAvailableBudget() - amount);
        }
        senderAccount.setTotalBudget(senderAccount.getTotalBudget() - amount);



        // 수신자 잔액 플러스
        receiverAccount.setTotalBudget(receiverAccount.getTotalBudget() + amount);
        receiverAccount.setAvailableBudget(receiverAccount.getAvailableBudget() + amount);

        // 통합 거래내역 생성
        TotalTransferHistory response = totalTransferHistoryService.save(
                TotalTransferHistoryRequestDTO.CreateTotalTransferHistory.builder()
                        .senderAccount(senderAccount)
                        .receiverAccount(receiverAccount)
                        .transferAmount(amount)
                        .transferType(transferType)
                        .build()
        );

        // 각 계좌 거래내역 생성
        accountTransferHistoryService.save(
                AccountTransferHistoryRequestDTO.CreateAccountTransferHistory.builder()
                        .ownerAccount(senderAccount)
                        .oppenentAccount(receiverAccount)
                        .transferAmount(amount)
                        .transferType(transferType)
                        .budgetAfterTransfer(senderAccount.getAvailableBudget())
                        .memo("")
                        .build()
        );
        accountTransferHistoryService.save(
                AccountTransferHistoryRequestDTO.CreateAccountTransferHistory.builder()
                        .ownerAccount(receiverAccount)
                        .oppenentAccount(senderAccount)
                        .transferAmount(-amount)
                        .transferType(transferType)
                        .budgetAfterTransfer(receiverAccount.getAvailableBudget())
                        .memo("")
                        .build()
        );

        return TotalTransferHistoryResponseDTO.GetTotalTransferHistory.fromEntity(response);
    }



}
