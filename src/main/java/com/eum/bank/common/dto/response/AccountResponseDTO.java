package com.eum.bank.common.dto.response;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AccountResponseDTO {
    // 계좌 생성 응답
    @Builder
    @Getter
    public static class Create {
        @NotEmpty(message = "계좌 번호가 생성되어야 합니다.")
        private String accountNumber;
    }
}
