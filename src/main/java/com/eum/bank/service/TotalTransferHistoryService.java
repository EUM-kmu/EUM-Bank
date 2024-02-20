package com.eum.bank.service;

import com.eum.bank.common.dto.request.TotalTransferHistoryRequestDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.TotalTransferHistory;
import com.eum.bank.repository.TotalTransferHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalTransferHistoryService {
    private final TotalTransferHistoryRepository totalTransferHistoryRepository;

    public TotalTransferHistory generateTotalHistory(Account senderAccount, Account receiverAccount, Long amount, String transferType) {
         return save(new TotalTransferHistoryRequestDTO
                .CreateTotalTransferHistory(senderAccount, receiverAccount, amount, transferType));
    }

    public TotalTransferHistory save(TotalTransferHistoryRequestDTO.CreateTotalTransferHistory dto) {
        return totalTransferHistoryRepository.save(dto.toEntity());
    }
}
