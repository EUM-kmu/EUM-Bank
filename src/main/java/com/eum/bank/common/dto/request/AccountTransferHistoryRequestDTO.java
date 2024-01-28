package com.eum.bank.common.dto.request;

import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

public class AccountTransferHistoryRequestDTO {

    // 내역 생성
    @Getter
    @Builder
    public static class CreateAccountTransferHistory {
        @NotEmpty(message = "거래 내역을 생성할 계좌를 입력해주세요.")
        private Account ownerAccount;
        @NotEmpty(message = "거래 상대 계좌를 입력해주세요.")
        private Account oppenentAccount;
        @NotEmpty(message = "거래 금액을 입력해주세요.")
        private Long transferAmount;
        @NotEmpty(message = "거래 유형을 입력해주세요.")
        private String transferType;
        @NotEmpty(message = "거래 후 잔액을 입력해주세요.")
        private Long budgetAfterTransfer;
        @NotEmpty(message = "거래 메모를 입력해주세요.")
        private String memo;

        // toEntity
        public AccountTransferHistory toEntity() {
            return AccountTransferHistory.builder()
                    .ownerAccount(ownerAccount)
                    .oppenentAccount(oppenentAccount)
                    .transferAmount(transferAmount)
                    .transferType(transferType)
                    .budgetAfterTransfer(budgetAfterTransfer)
                    .memo(memo)
                    .build();
        }
    }

}
