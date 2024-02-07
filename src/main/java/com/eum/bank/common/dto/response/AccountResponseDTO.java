package com.eum.bank.common.dto.response;

import com.eum.bank.domain.account.entity.Account;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

public class AccountResponseDTO {
    // 계좌 생성 응답
    @Builder
    @Getter
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
    public static class transfer {
        private String senderAccountNumber;
        private String receiverAccountNumber;
        private Long amount;
        private String password;
        private String transferType;
    }
}
