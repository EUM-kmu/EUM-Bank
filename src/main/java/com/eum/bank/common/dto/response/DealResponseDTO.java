package com.eum.bank.common.dto.response;

import jakarta.validation.GroupSequence;
import lombok.Builder;
import lombok.Getter;

public class DealResponseDTO {

    @Getter
    @Builder
    public static class Create {
        // 거래 id
        private Long dealId;
    }
}
