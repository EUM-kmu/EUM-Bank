package com.eum.bank.timeBank.controller.dto.request;

import com.eum.bank.timeBank.domain.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class RemittanceRequestDto {

    @Schema(description = "qr 송금 요청")
    @Builder
    @Getter
    public static class QRRemittance {
        @Schema(description = "hash-based mac", example = "")
        @NotEmpty
        private String hmac;

        @Schema(description = "원본 데이터", example = "<accountNumber>%<password>%<amount>%<receiverAccountNumber>")
        @NotEmpty
        private String remittanceInfo;

    }

    @Schema(description = "특정 계좌의 거래 내역 보기")
    @Getter
    public static class History {

        @Schema(description = "계좌 id", example = "123456789012")
        @NotEmpty
        private String accountId;

    }

    @Schema(description = "is sender or not")
    @Getter
    @Setter
    public static class isSender {

        @Schema(description = "is sender or not", example = "")
        @NotEmpty
        private boolean isSender;

    }
}
