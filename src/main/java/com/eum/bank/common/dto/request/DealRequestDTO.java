package com.eum.bank.common.dto.request;

import lombok.Builder;
import lombok.Getter;

public class DealRequestDTO {
    @Builder
    @Getter
    public static class  Create{
        //송금자 계좌번호
        private String accountNumber;
        // 비밀번호
        private String password;
        // 예치금 ( 지급해야 하는 금액 )
        private Long deposit;
        // 최대 인원수
        private Long maxPeople;
        // 글 id
        private Long postId;
    }

}
