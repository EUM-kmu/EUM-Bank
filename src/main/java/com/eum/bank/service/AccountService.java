package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.TotalTransferHistory;
import com.eum.bank.exception.BlockAccountException;
import com.eum.bank.exception.InsufficientAmountException;
import com.eum.bank.exception.WrongPasswordException;
import com.eum.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Random;

import static com.eum.bank.common.Constant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTransferHistoryService accountTransferHistoryService;
    private final TotalTransferHistoryService totalTransferHistoryService;

    /**
     * 계좌 생성
     * @param password
     * @return
     */
    public APIResponse<AccountResponseDTO.Create> createAccount(String password) {

        String accountNumber = this.generateAccountNumber();

        Account account = Account.initializeAccount(accountNumber, passwordEncoder.encode(password));

        accountRepository.save(account);

        AccountResponseDTO.Create response = new AccountResponseDTO.Create(account.getAccountNumber());

        return APIResponse.of(SuccessCode.INSERT_SUCCESS, response);
    }

    /**
     * 자유 송금
     * 1. 송금자 계좌, 수신자 계좌 상태 검증
     * 2. 송금자 잔액 확인
     * 3. 송금자 전체금액, 가용금액 마이너스
     * 4. 수신자 전체금액, 가용금액 플러스
     * 5. 통합 거래내역 생성, 각 계좌 거래내역 생성
     */
    @Transactional
    public TotalTransferHistoryResponseDTO.GetTotalTransferHistory transfer(AccountResponseDTO.Transfer transfer) {

        Long amount = transfer.getAmount();
        String password = transfer.getPassword();
        String transferType = transfer.getTransferType();

        Account senderAccount = matchAccountPassword(transfer.getSenderAccountNumber(), password);
        Account receiverAccount = validateAccount(transfer.getReceiverAccountNumber());

        // 송금자 잔액 검증
        validatePayment(senderAccount, amount);

        // 송금자 잔액 마이너스
        changeBudget(senderAccount, amount, DECREASE);

        // 수신자 잔액 플러스
        changeBudget(receiverAccount, amount, INCREASE);

        // 통합 거래내역 생성
        TotalTransferHistory response = totalTransferHistoryService.generateTotalHistory(senderAccount, receiverAccount, amount, transferType);

        accountTransferHistoryService.generateAccountHistory(senderAccount, receiverAccount, amount, transferType);

        return TotalTransferHistoryResponseDTO.GetTotalTransferHistory.fromEntity(response);
    }

    public String generateAccountNumber() {
        Random random = new Random();
        StringBuilder uniqueNumber;

        do {
            uniqueNumber = new StringBuilder();
            for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
                int digit = random.nextInt(10);
                uniqueNumber.append(digit);
            }

        } while (accountRepository.findByAccountNumber(uniqueNumber.toString()).isPresent());

        return uniqueNumber.toString();
    }

    public Boolean validateAccountNumber(String accountNumber) {
        if (accountNumber.length() != ACCOUNT_NUMBER_LENGTH) {
            return false;
        }

        return accountRepository.findByAccountNumber(accountNumber).isEmpty();
    }

    // 계좌 검증 (계좌 존재여부 + 블락 여부)
    public Account validateAccount(String accountNumber) {
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account number : " + accountNumber));

        if(account.getIsBlocked()){
            throw new BlockAccountException("Blocked account : " + accountNumber);
        }
        return account;
    }

    // 계좌번호 와 비밀번호 매치
    public Account matchAccountPassword(String accountNumber, String password) {
        Account account = this.validateAccount(accountNumber);

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new WrongPasswordException("Invalid password");
        }

        return account;
    }

    // 계좌번호와 비밀번호로 계좌 조회
    public APIResponse<AccountResponseDTO.AccountInfo> getAccount(String accountNumber, String password) {
        Account account = this.matchAccountPassword(accountNumber, password);

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, AccountResponseDTO.AccountInfo.builder()
                .accountNumber(account.getAccountNumber())
                .totalBudget(account.getTotalBudget())
                .availableBudget(account.getAvailableBudget())
                .build());
    }


    /**
     * 지불 능력 판단
     * @param account
     * @param amount
     */
    public void validatePayment(Account account, Long amount) {
        if (account.getAvailableBudget() < amount && account.getTotalBudget() < amount) {
            throw new InsufficientAmountException("Invalid amount");
        }
    }

    /**
     * 전체 예산 변경
     * @param account
     * @param amount
     */
    public void changeBudget(Account account, Long amount, String transferType) {
        if(transferType.equals(DECREASE)){
            validatePayment(account, amount);
            amount = -amount;
        }

        account.setTotalBudget(account.getTotalBudget() + amount);
        account.setAvailableBudget(account.getAvailableBudget() + amount);
    }

    /**
     * 가용금액 변경
     * @param account
     * @param amount
     */
    public void changeAvailableBudget(Account account, Long amount, String transferType) {
        if(transferType.equals(DECREASE)){
            validatePayment(account, amount);
            amount = -amount;
        }

        account.setAvailableBudget(account.getAvailableBudget() + amount);
    }

    /**
     * 계좌 동결
     * @param accountNumber
     * 아직 자세한 로직 없이 계좌번호 받아서 동결 기능
     * @return
     */
    public AccountResponseDTO.Block blockAccount(String accountNumber) {
        Account account = this.validateAccount(accountNumber);

        account.setIsBlocked(true);

        return AccountResponseDTO.Block.builder()
                .accountNumber(account.getAccountNumber())
                .build();

    }
}
