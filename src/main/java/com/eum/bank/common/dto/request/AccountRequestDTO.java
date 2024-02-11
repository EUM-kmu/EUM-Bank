package com.eum.bank.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

public class AccountRequestDTO {
    // 계좌 생성 요청
    @Schema(description = "계좌 생성 요청")
    @Getter
    public static class CreateAccount {
        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    // 계좌 조회 요청
    @Schema(description = "계좌 조회 요청")
    @Getter
    public static class GetAccount {
        @Schema(description = "계좌 번호")
        @NotEmpty(message = "계좌 번호를 입력해주세요.")
        private String accountNumber;

        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Schema(description = "자유 송금 요청")
    @Builder
    @Getter
    public static class Transfer {
        @Schema(description = "계좌 번호")
        @NotEmpty(message = "계좌 번호를 입력해주세요.")
        private String accountNumber;

        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;

        @Schema(description = "송금 금액")
        @NotEmpty(message = "송금 금액을 입력해주세요.")
        private Long amount;

        // 수신자 계좌 번호
        @Schema(description = "수신 계좌 번호")
        @NotEmpty(message = "송금할 계좌 번호를 입력해주세요.")
        private String receiverAccountNumber;

    }
}
