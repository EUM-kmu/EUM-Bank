package com.eum.bank.common.dto.request;

import com.eum.bank.common.Constant;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AccountTransferHistoryRequestDTO {

    // 내역 생성
    @Getter
    @Builder
    @Schema(description = "거래 내역 생성 요청")
    public static class CreateAccountTransferHistory {
        @NotEmpty(message = "거래 내역을 생성할 계좌를 입력해주세요.")
        @Schema(description = "거래 내역을 생성할 계좌", example = "123456789012")
        private Account ownerAccount;

        @NotEmpty(message = "거래 상대 계좌를 입력해주세요.")
        @Schema(description = "거래 상대 계좌", example = "123456789012")
        private Account oppenentAccount;

        @NotEmpty(message = "거래 금액을 입력해주세요.")
        @Schema(description = "거래 금액", example = "10000")
        private Long transferAmount;

        @NotEmpty(message = "거래 유형을 입력해주세요.")
        @Schema(description = "거래 유형", example = Constant.FREE_TYPE)
        private String transferType;

        @NotEmpty(message = "거래 후 잔액을 입력해주세요.")
        @Schema(description = "거래 후 잔액", example = "10000")
        private Long budgetAfterTransfer;

        @NotEmpty(message = "거래 메모를 입력해주세요.")
        @Schema(description = "거래 메모", example = "거래 내역 생성")
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

        public static CreateAccountTransferHistory generateWithSender(Account senderAccount, Account receiverAccount, Long transferAmount, String transferType){
            return CreateAccountTransferHistory.builder()
                    .ownerAccount(senderAccount)
                    .oppenentAccount(receiverAccount)
                    .transferAmount(transferAmount)
                    .transferType(transferType)
                    .budgetAfterTransfer(senderAccount.getAvailableBudget())
                    .memo("")
                    .build();
        }

        public static CreateAccountTransferHistory generateWithReceiver(Account senderAccount, Account receiverAccount, Long transferAmount, String transferType){
            return CreateAccountTransferHistory.builder()
                    .ownerAccount(receiverAccount)
                    .oppenentAccount(senderAccount)
                    .transferAmount(transferAmount)
                    .transferType(transferType)
                    .budgetAfterTransfer(receiverAccount.getAvailableBudget())
                    .memo("")
                    .build();
        }
    }

}
