package com.eum.bank.timeBank.controller.dto.response;

import com.eum.bank.domain.account.entity.AccountTransferHistory;
import com.eum.bank.timeBank.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TransactionHistoryResponseDto {
/*

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RemittanceResult{

        private String senderAccountId;
        private String receiverAccountId;
        private int amount;
        private int senderBalanceAfter;
        private int receiverBalanceAfter;
        private LocalDateTime createdAt;


        public static RemittanceResult from(TransactionHistory response) {
            return RemittanceResult.builder()
                    .senderAccountId(response.getSenderAccountId())
                    .receiverAccountId(response.getReceiverAccountId())
                    .amount(response.getAmount())
                    .senderBalanceAfter(response.getSenderBalanceAfter())
                    .receiverBalanceAfter(response.getReceiverBalanceAfter())
                    .createdAt(response.getCreatedAt())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RemittanceResultWithUserInfo{

        private Long amount;
        private boolean isSender;

        private String senderNickname;
        private String senderProfileImg;
        private int senderBalanceAfter;

        private String receiverNickname;
        private String receiverProfileImg;
        private int receiverBalanceAfter;

        private LocalDateTime createdAt;


        public static RemittanceResultWithUserInfo senderInfoFrom(AccountTransferHistory response, User user) {
            return RemittanceResultWithUserInfo.builder()
                    .amount(response.getTransferAmount())
                    .isSender(false)
                    .senderNickname(user.getUserNickname())
                    .senderProfileImg(user.getUserProfileImg())
                    .receiverBalanceAfter(response.getReceiverBalanceAfter())
                    .createdAt(response.getCreatedAt())
                    .build();
        }

        public static RemittanceResultWithUserInfo receiverInfoFrom(AccountTransferHistory response, User user) {
            return RemittanceResultWithUserInfo.builder()
                    .amount(response.getTransferAmount())
                    .isSender(true)
                    .receiverNickname(user.getUserNickname())
                    .receiverProfileImg(user.getUserProfileImg())
                    .senderBalanceAfter(response.getSenderBalanceAfter())
                    .createdAt(response.getCreatedAt())
                    .build();
        }
    }

 */

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RemittanceList{

        private Long transId;
        private LocalDateTime createdAt;
        private Long postBalance;
        private boolean send;
        private String senderNickname;
        private String senderProfileImg;
        private String receiverNickname;
        private String receiverProfileImg;
        private Long amount;

        public static RemittanceList senderInfoFrom(AccountTransferHistory response, User user) {
            return RemittanceList.builder()
                    .transId(response.getId())
                    .createdAt(response.getCreatedAt())
                    .postBalance(response.getBudgetAfterTransfer())
                    .send(false)
                    .senderNickname(user.getUserNickname())
                    .senderProfileImg(user.getUserProfileImg())
                    .amount(response.getTransferAmount())
                    .build();
        }

        public static RemittanceList receiverInfoFrom(AccountTransferHistory response, User user) {
            return RemittanceList.builder()
                    .transId(response.getId())
                    .createdAt(response.getCreatedAt())
                    .postBalance(response.getBudgetAfterTransfer())
                    .send(true)
                    .receiverNickname(user.getUserNickname())
                    .receiverProfileImg(user.getUserProfileImg())
                    .amount(response.getTransferAmount())
                    .build();
        }
    }
}
