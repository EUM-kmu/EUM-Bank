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

    public APIResponse<AccountResponseDTO.AccountInfo> currentAccount(String userId) throws FeignException {

        HaetsalResponseDto.Profile userInfo = haetsalClient.getProfile(userId).getData();

        Account account = accountService.validateAccount(userInfo.getAccountNumber());

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, AccountResponseDto.AccountInfo.from(account,userInfo));


    }
}
