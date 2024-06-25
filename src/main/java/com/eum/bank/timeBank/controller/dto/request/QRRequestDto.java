package com.eum.bank.timeBank.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class QRRequestDto {

    @Schema(description = "qr 스캔 시")
    @Getter
    public static class QRCodeWithSenderInfo {

        @Schema(description = "hash-based mac", example = "")
        @NotEmpty
        private String hmac;

        @Schema(description = "송금 받을 유저 정보", example = "<userId>%<accountId>%<createdAt>")
        @NotEmpty
        private String userInfo;

        @Schema(description = "송금 보낼 유저id", example = "123456789012")
        @NotEmpty
        private String senderAccountId;
    }

    @Schema(description = "qr 생성을 위한 account 정보")
    @Getter
    public static class BaseInfo {

        @Schema(description = "유저의 계좌 id", example = "123456789012")
        @NotEmpty
        private String accountId;
    }
}
