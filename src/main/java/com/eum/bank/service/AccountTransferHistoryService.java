package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.AccountTransferHistoryRequestDTO;
import com.eum.bank.common.enums.ErrorCode;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.account.entity.AccountTransferHistory;
import com.eum.bank.repository.AccountRepository;
import com.eum.bank.repository.AccountTransferHistoryRepository;
import com.eum.bank.timeBank.client.HaetsalClient;
import com.eum.bank.timeBank.client.HaetsalResponseDto;
import com.eum.bank.timeBank.domain.User;
import com.eum.bank.timeBank.controller.dto.request.RemittanceRequestDto;
import com.eum.bank.timeBank.controller.dto.response.TransactionHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountTransferHistoryService {

    private final AccountTransferHistoryRepository accountTransferHistoryRepository;
    private final AccountRepository accountRepository;

    private final HaetsalClient haetsalClient;

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
                transferHistories = accountTransferHistoryRepository.findByOwnerAccount_AccountNumberOrderByIdDesc(dto.getAccountId());
                break;
//            case SEND:
//                transferHistories = accountTransferHistoryRepository.findBySenderAccount_AccountNumber(dto.getAccountId());
//                break;
//            case RECEIVE:
//                transferHistories = accountTransferHistoryRepository.findByReceiverAccount_AccountNumber(dto.getAccountId());
        }

        List<String> opponentAccountNumbers = transferHistories.stream()
                .map(history -> history.getOppenentAccount().getAccountNumber())
                .toList();
        List<HaetsalResponseDto.UserInfo> userInfos = haetsalClient.getUserInfos(opponentAccountNumbers);

        log.error("Size mismatch: transferHistories size is {}, but userInfos size is {}" +
                        "\nError caused by userAccountId: {}",
                transferHistories.size(), userInfos.size(), dto.getAccountId());

        List<TransactionHistoryResponseDto.RemittanceList> list = new ArrayList<>();
        for (int i = 0; i < transferHistories.size(); i++) {
            AccountTransferHistory history = transferHistories.get(i);
            HaetsalResponseDto.UserInfo userInfo = userInfos.get(i);
            User user = new User(userInfo.getNickName(), userInfo.getProfileImage());

            if (history.getTransferAmount() < 0) { // 내가 보낸 경우
                list.add(TransactionHistoryResponseDto.RemittanceList.receiverInfoFrom(history, user));
            } else { // 내가 받은 경우
                list.add(TransactionHistoryResponseDto.RemittanceList.senderInfoFrom(history, user));
            }
        }

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, list);
    }
}
