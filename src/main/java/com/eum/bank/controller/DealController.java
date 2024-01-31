package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.common.dto.response.DealResponseDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.service.AccountService;
import com.eum.bank.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DealRequestDTO.Create create) {

        String accountNumber = create.getAccountNumber();
        String password = create.getPassword();
        Long deposit = create.getDeposit();
        Long maxPeople = create.getMaxPeople();
        Long postId = create.getPostId();

        APIResponse<?> apiResponse = accountService.getAccount(accountNumber, password);

        Account account = accountService.getAccount(accountNumber);

        if (apiResponse.getStatus() == 200){
            // 계좌가 존재하고 비밀번호가 일치하는 경우
            // 거래 생성
            dealService.createDeal(account, deposit, maxPeople, postId);
        } else if (apiResponse.getStatus() == 400){
            // 계좌가 존재하지 않는 경우
            return ResponseEntity.badRequest().body(apiResponse);
        } else if (apiResponse.getStatus() == 401){
            // 비밀번호가 일치하지 않는 경우
            return ResponseEntity.status(401).body(apiResponse);
        }

        APIResponse<DealResponseDTO.Create> response = new APIResponse<>();
        return ResponseEntity.ok(response);
    }
}
