package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.eum.bank.common.Constant.FREE_TYPE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    /**
     * 계좌 생성
     * @param createAccount
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountRequestDTO.CreateAccount createAccount) {

        String password = createAccount.getPassword();
        APIResponse<?> response = accountService.createAccount(password);

        return ResponseEntity.ok(response);
    }

    // 계좌 조회
    @GetMapping
    public ResponseEntity<?> getAccountInfo(@RequestBody AccountRequestDTO.GetAccount getAccountInfo) {
        String accountNumber = getAccountInfo.getAccountNumber();
        String password = getAccountInfo.getPassword();

        APIResponse<?> response = accountService.getAccount(accountNumber, password);

        return ResponseEntity.ok(response);
    }

    // 자유 송금
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody AccountRequestDTO.Transfer transfer) {
        TotalTransferHistoryResponseDTO.GetTotalTransferHistory response = accountService.transfer(transfer.getAccountNumber(), transfer.getReceiverAccountNumber(), transfer.getAmount(), transfer.getPassword(), "a");
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS, response));
    }

}
