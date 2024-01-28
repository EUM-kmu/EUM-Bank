package com.eum.bank.common.dto.request;

import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.TotalTransferHistory;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

public class TotalTransferHistoryRequestDTO {

    @Getter
    @Builder
    public static class CreateTotalTransferHistory {
        @NotEmpty(message = "송금자 계좌를 입력해주세요.")
        private Account senderAccount;
        @NotEmpty(message = "수취자 계좌를 입력해주세요.")
        private Account receiverAccount;
        @NotEmpty(message = "송금 금액을 입력해주세요.")
        private Long transferAmount;
        @NotEmpty(message = "거래유형을 입력해주세요.")
        private String transferType;

        // toEntity
        public TotalTransferHistory toEntity() {
            return TotalTransferHistory.builder()
                    .senderAccount(senderAccount)
                    .receiverAccount(receiverAccount)
                    .transferAmount(transferAmount)
                    .transferType(transferType)
                    .build();
        }
    }
}
