package com.eum.bank.common.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class DealRequestDTO {

    @Getter
    public static class completeDeal{
        // 거래ID
        @NotEmpty(message = "거래ID를 입력해주세요.")
        private Long dealId;
        // 예치금
        @NotEmpty(message = "예치금을 입력해주세요.")
        private Long deposit;
        // 수신 계좌번호 리스트
        @NotEmpty(message = "수신 계좌번호를 입력해주세요.")
        private String[] receiverAccountNumbers;
    }


    @Getter
    public static class updateDeal{
        // 거래ID
        @NotEmpty(message = "거래ID를 입력해주세요.")
        private Long dealId;
        // 송금자 계좌번호
        @NotEmpty(message = "송금자 계좌번호를 입력해주세요.")
        private String senderAccountNumber;
        // 비밀번호
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
        // 예치금
        @NotEmpty(message = "예치금을 입력해주세요.")
        private Long deposit;
        // 최대인원수
        @NotEmpty(message = "최대인원수를 입력해주세요.")
        private Long numberOfPeople;
    }
}
