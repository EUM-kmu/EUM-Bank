package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.eum.bank.common.PackageInfo.FREE_TYPE;
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {
    private final AccountService accountService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AccountRequestDTO.CreateAccount createAccount) {

        String password = createAccount.getPassword();
        APIResponse<?> response = accountService.createAccount(password);

        return ResponseEntity.ok(response);
    }

    // 계좌 조회
    @PostMapping("/getAccount")
    public ResponseEntity<?> getAccountInfo(@RequestBody AccountRequestDTO.GetAccount getAccountInfo) {
        String accountNumber = getAccountInfo.getAccountNumber();
        String password = getAccountInfo.getPassword();

        APIResponse<?> response = accountService.getAccount(accountNumber, password);

        return ResponseEntity.ok(response);
    }

    // 자유 송금
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody AccountRequestDTO.Transfer transfer) {
        String accountNumber = transfer.getAccountNumber();
        String password = transfer.getPassword();
        Long deposit = transfer.getDeposit();
        String receiverAccountNumber = transfer.getReceiverAccountNumber();

        accountService.getAccount(accountNumber, password);

        APIResponse<?> response = accountService.transfer(
                accountNumber, receiverAccountNumber, deposit, password, FREE_TYPE
        );

        return ResponseEntity.ok(response);
    }
}
