package com.eum.bank.timeBank.controller.dto.response;

import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.timeBank.client.HaetsalProfileResponse;
import lombok.Builder;
import lombok.Getter;

public class AccountResponseDto {

    @Builder
    @Getter
    public static class AccountInfo {
        private String nickName;
        private String accountNumber;
        private Long totalBudget;
        private Long availableBudget;
        private Boolean isBlocked;

        public static AccountInfo from(Account account, HaetsalProfileResponse.Data data) {
            return AccountInfo.builder()
                    .nickName(data.getNickName())
                    .accountNumber(account.getAccountNumber())
                    .totalBudget(account.getTotalBudget())
                    .availableBudget(account.getAvailableBudget())
                    .isBlocked(account.getIsBlocked())
                    .build();
        }
    }
}
