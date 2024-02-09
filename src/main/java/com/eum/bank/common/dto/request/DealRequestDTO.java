package com.eum.bank.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Builder;

public class DealRequestDTO {
    @Schema(description = "거래 생성 요청")
    @Builder
    @Getter
    public static class createDeal{
        // 송금자 계좌번호
        @Schema(description = "송금자 계좌번호")
        private String accountNumber;
        // 비밀번호
        @Schema(description = "비밀번호")
        private String password;
        // 예치금 ( 지급해야 하는 금액 )
        @Schema(description = "예치금")
        private Long deposit;
        // 최대 인원수
        @Schema(description = "최대 인원수")
        private Long maxPeople;
        // 글 id
        @Schema(description = "글 id")
        private Long postId;
    }

    @Schema(description = "거래 성사 요청")
    @Getter
    public static class completeDeal{
        // 거래ID
        @Schema(description = "거래ID")
        @NotEmpty(message = "거래ID를 입력해주세요.")
        private Long dealId;
        // 수신 계좌번호 리스트
        @Schema(description = "수신 계좌번호 리스트")
        @NotEmpty(message = "수신 계좌번호를 입력해주세요.")
        private String[] receiverAccountNumbers;
        // 비밀번호
        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Schema(description = "거래 수정 요청")
    @Getter
    public static class updateDeal{
        // 거래ID
        @Schema(description = "거래ID")
        @NotEmpty(message = "거래ID를 입력해주세요.")
        private Long dealId;
        // 송금자 계좌번호
        @Schema(description = "송금자 계좌번호")
        @NotEmpty(message = "송금자 계좌번호를 입력해주세요.")
        private String senderAccountNumber;
        // 비밀번호
        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
        // 예치금
        @Schema(description = "예치금")
        @NotEmpty(message = "예치금을 입력해주세요.")
        private Long deposit;
        // 최대인원수
        @Schema(description = "최대인원수")
        @NotEmpty(message = "최대인원수를 입력해주세요.")
        private Long numberOfPeople;
    }

    @Schema(description = "거래 취소 요청")
    @Getter
    public static class cancelDeal{
        // 거래ID
        @Schema(description = "거래ID")
        @NotEmpty(message = "거래ID를 입력해주세요.")
        private Long dealId;
        // 송금자 계좌번호
        @Schema(description = "송금자 계좌번호")
        @NotEmpty(message = "송금자 계좌번호를 입력해주세요.")
        private String senderAccountNumber;
        // 비밀번호
        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }


    @Getter
    public static class executeDeal{
        // 거래ID
        @Schema(description = "거래ID")
        @NotEmpty(message = "거래ID를 입력해주세요.")
        private Long dealId;
        // 송금자 계좌번호
        @Schema(description = "송금자 계좌번호")
        @NotEmpty(message = "송금자 계좌번호를 입력해주세요.")
        private String senderAccountNumber;
        // 비밀번호
        @Schema(description = "비밀번호")
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

}
