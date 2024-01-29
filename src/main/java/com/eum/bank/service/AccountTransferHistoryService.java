package com.eum.bank.service;

import com.eum.bank.common.dto.request.AccountTransferHistoryRequestDTO;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import com.eum.bank.repository.AccountTransferHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountTransferHistoryService {

    private final AccountTransferHistoryRepository accountTransferHistoryRepository;

    // 저장
    public AccountTransferHistory save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory dto) {
        return accountTransferHistoryRepository.save(dto.toEntity());
    }
}
