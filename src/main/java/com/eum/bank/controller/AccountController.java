package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.ErrorResponse;
import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.eum.bank.common.Constant.FREE_TYPE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "계좌", description = "계좌 관련 API")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    /**
     * 계좌 생성
     * @param createAccount
     * @return
     */
    @Operation(summary = "계좌 생성", description = "계좌를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "계좌 생성 성공")
    @ApiResponse(responseCode = "400", description = "계좌 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<APIResponse<AccountResponseDTO.Create>> create(
            @Schema(description = "계좌 생성 정보", required = true, implementation = AccountRequestDTO.CreateAccount.class)
            @RequestBody AccountRequestDTO.CreateAccount createAccount
    ) {

        String password = createAccount.getPassword();
        APIResponse<AccountResponseDTO.Create> response = accountService.createAccount(password);

        return ResponseEntity.status(201).body(response);
    }

    // 계좌 조회
    @Operation(summary = "계좌 조회", description = "계좌를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "계좌 조회 성공")
    @ApiResponse(responseCode = "400", description = "계좌 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<APIResponse<AccountResponseDTO.AccountInfo>> getAccountInfo(
            @RequestParam
            @Parameter(description = "계좌 번호", required = true)
            String accountNumber,
            @Parameter(description = "비밀번호", required = true)
            @RequestParam
            String password
    ) {

        APIResponse<AccountResponseDTO.AccountInfo> response = accountService.getAccount(accountNumber, password);

        return ResponseEntity.ok(response);
    }

    // 자유 송금
    @Operation(summary = "자유 송금", description = "자유 송금을 합니다.")
    @ApiResponse(responseCode = "200", description = "자유 송금 성공")
    @ApiResponse(responseCode = "400", description = "자유 송금 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/transfer")
    public ResponseEntity<APIResponse<TotalTransferHistoryResponseDTO.GetTotalTransferHistory>> transfer(
            @Schema(description = "송금 정보", required = true, implementation = AccountRequestDTO.Transfer.class)
            @RequestBody AccountRequestDTO.Transfer transfer) {
        AccountResponseDTO.Transfer transferResponse = AccountResponseDTO.Transfer.freeTransfer(transfer);

        TotalTransferHistoryResponseDTO.GetTotalTransferHistory response = accountService.transfer(transferResponse);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS, response));
    }

}
