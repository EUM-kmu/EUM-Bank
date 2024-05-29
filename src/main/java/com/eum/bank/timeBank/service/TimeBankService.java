package com.eum.bank.timeBank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.enums.ErrorCode;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.service.AccountService;
import com.eum.bank.timeBank.client.HaetsalClient;
import com.eum.bank.timeBank.client.HaetsalResponseDto;
import com.eum.bank.timeBank.controller.dto.response.AccountResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TimeBankService {

    private final HaetsalClient haetsalClient;
    private final AccountService accountService;

    public APIResponse<AccountResponseDTO.AccountInfo> currentAccount(String userId) {

        try {
            HaetsalResponseDto. ProfileResponseBody profileResponseBody = haetsalClient.getProfile(userId);
            boolean isSuccess = profileResponseBody.getCode().startsWith("2");
            if(!isSuccess){
                log.error("Cannot get profile from Haetsal-Service: " +
                        "\nresultMsg: {}, reason: {}" +
                        "\nError Caused by userId: {}",
                        profileResponseBody.getDetailMsg(), profileResponseBody.getReason(), userId);
                return APIResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,
                        "resultMsg: " + profileResponseBody.getDetailMsg() +
                        ", reason: " + profileResponseBody.getReason());
            }

            HaetsalResponseDto.Profile userInfo = profileResponseBody.getData();

            Account account = accountService.validateAccount(userInfo.getAccountNumber());

            return APIResponse.of(SuccessCode.SELECT_SUCCESS, AccountResponseDto.AccountInfo.from(account,userInfo));

        }catch (FeignException e){
            return APIResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,
                    "채팅 외부 서비스(햇살 서버의 /profile)를 불러오는 데 실패했습니다:  " + e.getMessage());
        }

    }
}
