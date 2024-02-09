package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "계좌 생성", description = "계좌를 생성합니다.")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountRequestDTO.CreateAccount createAccount) {

        String password = createAccount.getPassword();
        APIResponse<?> response = accountService.createAccount(password);

        return ResponseEntity.ok(response);
    }

    // 계좌 조회
    @Operation(summary = "계좌 조회", description = "계좌를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getAccountInfo(@RequestBody AccountRequestDTO.GetAccount getAccountInfo) {
        String accountNumber = getAccountInfo.getAccountNumber();
        String password = getAccountInfo.getPassword();

        APIResponse<?> response = accountService.getAccount(accountNumber, password);

        return ResponseEntity.ok(response);
    }

    // 자유 송금
    @Operation(summary = "자유 송금", description = "자유 송금을 합니다.")
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody AccountRequestDTO.Transfer transfer) {
        AccountResponseDTO.transfer transferResponse = AccountResponseDTO.transfer.builder()
                .senderAccountNumber(transfer.getAccountNumber())
                .receiverAccountNumber(transfer.getReceiverAccountNumber())
                .amount(transfer.getAmount())
                .password(transfer.getPassword())
                .transferType(FREE_TYPE)
                .build();

        TotalTransferHistoryResponseDTO.GetTotalTransferHistory response = accountService.transfer(transferResponse);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS, response));
    }

}
