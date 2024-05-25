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
        @Schema(description = "보내는 사람 account Id", example = "123456789012")
        @NotEmpty
        private String senderAccountId;

        @Schema(description = "보내는 금액", example = "60")
        @NotEmpty
        private int amount;

        @Schema(description = "받는 사람 account Id", example = "123456789012")
        @NotEmpty
        private String receiverAccountId;
    }

    @Schema(description = "특정 계좌의 거래 내역 보기")
    @Getter
    public static class History {

        @Schema(description = "보기 옵션", example = "ALL :전체 내역보기, SEND :보낸 내역만 보기, RECEIVE :받은 내역만 보기")
        @NotEmpty
        private TransactionType type;

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
