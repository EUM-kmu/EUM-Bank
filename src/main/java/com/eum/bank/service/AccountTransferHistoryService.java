package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.AccountTransferHistoryRequestDTO;
import com.eum.bank.common.enums.ErrorCode;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import com.eum.bank.repository.AccountRepository;
import com.eum.bank.repository.AccountTransferHistoryRepository;
import com.eum.bank.timeBank.domain.User;
import com.eum.bank.timeBank.controller.dto.request.RemittanceRequestDto;
import com.eum.bank.timeBank.controller.dto.response.TransactionHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTransferHistoryService {

    private final AccountTransferHistoryRepository accountTransferHistoryRepository;
    private final AccountRepository accountRepository;

    public void generateAccountHistory(Account senderAccount, Account receiverAccount, Long amount, String transferType) {
        // 각 계좌 거래내역 생성
        save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory
                .generateWithSender(senderAccount, receiverAccount, -amount, transferType)
        );

        save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory
                .generateWithReceiver(senderAccount, receiverAccount, amount, transferType)
        );

    }

    // 저장
    public void save(AccountTransferHistoryRequestDTO.CreateAccountTransferHistory dto) {
        accountTransferHistoryRepository.save(dto.toEntity());
    }

    public APIResponse getUserHistory(RemittanceRequestDto.History dto) {

        if(accountRepository.findByAccountNumber(dto.getAccountId()).isEmpty()){
            return APIResponse.of(ErrorCode.INVALID_PARAMETER, "존재하지 않는 계좌번호입니다.");
        }

        List<AccountTransferHistory> transferHistories = new ArrayList<>();

        switch (dto.getType()){
            case ALL:
                transferHistories = accountTransferHistoryRepository.findByOwnerAccount_AccountNumber(dto.getAccountId());
                break;
//            case SEND:
//                transferHistories = accountTransferHistoryRepository.findBySenderAccount_AccountNumber(dto.getAccountId());
//                break;
//            case RECEIVE:
//                transferHistories = accountTransferHistoryRepository.findByReceiverAccount_AccountNumber(dto.getAccountId());
        }

        User user = new User(null, null);

        List<TransactionHistoryResponseDto.RemittanceList> list = transferHistories.stream()
                .map( i -> {
                    if( i.getTransferAmount() < 0){ // 내가 보낸이면
                        // TODO: User api 완성 시 연결
//                        UserClientResponseDto.UserInfo receiverInfo = haetsalClient.getProfile(i.getReceiverAccountId());
//                        user.setUserNickname(receiverInfo.getNickname());
//                        user.setUserProfileImg(receiverInfo.getProfile_img());
                        return TransactionHistoryResponseDto.RemittanceList.receiverInfoFrom(i, user);
                    }else{
//                        UserClientResponseDto.UserInfo senderInfo = haetsalClient.getProfile(i.getSenderAccountId()));
//                        user.setUserNickname(senderInfo.getNickname());
//                        user.setUserProfileImg(senderInfo.getProfile_img());
                        return TransactionHistoryResponseDto.RemittanceList.senderInfoFrom(i, user);
                    }
                })
                .toList();

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, list);
    }
}
