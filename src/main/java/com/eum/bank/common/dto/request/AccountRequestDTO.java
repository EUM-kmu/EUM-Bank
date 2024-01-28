package com.eum.bank.common.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class AccountRequestDTO {
    // 계좌 생성 요청
    @Getter
    public static class CreateAccount {
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private Long password;
    }
}
