package com.eum.bank.common.dto.response;

import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.deal.entity.Deal;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.eum.bank.common.Constant.BATCH_TYPE;
import static com.eum.bank.common.Constant.FREE_TYPE;

public class AccountResponseDTO {
    // 계좌 생성 응답
    @Builder
    @Getter
    @AllArgsConstructor
    public static class Create {
        @NotEmpty(message = "계좌 번호가 생성되어야 합니다.")
        private String accountNumber;

    }

    // 계좌 조회 응답
    @Builder
    @Getter
    public static class AccountInfo {
        private String accountNumber;
        private Long totalBudget;
        private Long availableBudget;
        private Boolean isBlocked;

        // fromEntity
        public static AccountInfo fromEntity(Account account) {
            return AccountInfo.builder()
                    .accountNumber(account.getAccountNumber())
                    .totalBudget(account.getTotalBudget())
                    .availableBudget(account.getAvailableBudget())
                    .isBlocked(account.getIsBlocked())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class Transfer {
        private String senderAccountNumber;
        private String receiverAccountNumber;
        private Long amount;
        private String password;
        private String transferType;

        public static Transfer freeTransfer(AccountRequestDTO.Transfer requestTransfer){
            return AccountResponseDTO.Transfer.builder()
                    .senderAccountNumber(requestTransfer.getAccountNumber())
                    .receiverAccountNumber(requestTransfer.getReceiverAccountNumber())
                    .amount(requestTransfer.getAmount())
                    .password(requestTransfer.getPassword())
                    .transferType(FREE_TYPE)
                    .build();
        }
        public static Transfer batchTransfer(Deal deal, String receiverAccountNumber, String password){
            return Transfer.builder()
                    .senderAccountNumber(deal.getSenderAccount().getAccountNumber())
                    .receiverAccountNumber(receiverAccountNumber)
                    .amount(deal.getDeposit())
                    .password(password)
                    .transferType(BATCH_TYPE)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class Block {
        private String accountNumber;
    }
}
