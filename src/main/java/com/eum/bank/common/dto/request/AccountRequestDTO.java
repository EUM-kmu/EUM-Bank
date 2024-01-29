package com.eum.bank.common.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class AccountRequestDTO {
    // 계좌 생성 요청
    @Getter
    public static class CreateAccount {
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    // 계좌 조회 요청
    @Getter
    public static class GetAccount {
        @NotEmpty(message = "계좌 번호를 입력해주세요.")
        private String accountNumber;
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }
}
