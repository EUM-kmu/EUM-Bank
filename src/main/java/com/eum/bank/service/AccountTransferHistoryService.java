package com.eum.bank.service;

import com.eum.bank.common.dto.request.AccountTransferHistoryRequestDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import com.eum.bank.repository.AccountTransferHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountTransferHistoryService {

    private final AccountTransferHistoryRepository accountTransferHistoryRepository;

    public void generateAccountHistory(Account senderAccount, Account receiverAccount, Long amount, String transferType) {
        // 각 계좌 거래내역 생성
        save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory
                .generateWithSender(senderAccount, receiverAccount, -amount, transferType)
        );

        save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory
                .generateWithReceiver(senderAccount, receiverAccount, amount, transferType)
        );

    }

    // 저장
    public void save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory dto) {
        accountTransferHistoryRepository.save(dto.toEntity());
    }
}
