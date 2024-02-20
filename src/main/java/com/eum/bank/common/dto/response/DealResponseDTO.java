package com.eum.bank.common.dto.response;

import jakarta.validation.GroupSequence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DealResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class createDeal {
        // 거래 id
        private Long dealId;
    }
}
