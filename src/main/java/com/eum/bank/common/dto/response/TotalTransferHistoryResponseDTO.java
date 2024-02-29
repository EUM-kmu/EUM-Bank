package com.eum.bank.common.dto.response;

import com.eum.bank.domain.account.entity.TotalTransferHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TotalTransferHistoryResponseDTO {

    // 거래 내역 반환
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetTotalTransferHistory {
        private Long id;
        private AccountResponseDTO.AccountInfo senderAccount;
        private AccountResponseDTO.AccountInfo receiverAccount;
        private Long transferAmount;
        private String transferType;

        // fromEntity
        public static GetTotalTransferHistory fromEntity(TotalTransferHistory totalTransferHistory) {
            return GetTotalTransferHistory.builder()
                    .id(totalTransferHistory.getId())
                    .senderAccount(AccountResponseDTO.AccountInfo.fromEntity(totalTransferHistory.getSenderAccount()))
                    .receiverAccount(AccountResponseDTO.AccountInfo.fromEntity(totalTransferHistory.getReceiverAccount()))
                    .transferAmount(totalTransferHistory.getTransferAmount())
                    .transferType(totalTransferHistory.getTransferType())
                    .build();
        }
    }
}
