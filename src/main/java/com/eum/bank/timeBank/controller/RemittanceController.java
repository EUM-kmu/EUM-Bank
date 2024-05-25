package com.eum.bank.timeBank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.ErrorResponse;
import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.service.AccountService;
import com.eum.bank.service.AccountTransferHistoryService;
import com.eum.bank.timeBank.controller.dto.request.RemittanceRequestDto;
import com.eum.bank.timeBank.controller.dto.response.TransactionHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timebank-service/api/remittance")
@Tag(name = "타임뱅크" ,description = "타임뱅크용 api")
public class RemittanceController {

    private final AccountService accountService;
    private final AccountTransferHistoryService accountTransferHistoryService;

    // 송금
    @Operation(summary = "자유 송금", description = "자유 송금을 합니다.")
    @PostMapping("/qr")
    public ResponseEntity<APIResponse<TotalTransferHistoryResponseDTO.GetTotalTransferHistory>> QRRemittance(
            @Schema(description = "송금 요청", required = true)
            @RequestHeader String userId,
            @RequestBody AccountRequestDTO.Transfer transfer
    ) {

        AccountResponseDTO.Transfer transferResponse = AccountResponseDTO.Transfer.freeTransfer(transfer);

        TotalTransferHistoryResponseDTO.GetTotalTransferHistory response = accountService.transfer(transferResponse);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS, response));

    }

    // 특정 계좌의 거래 내역 확인
    @Operation(summary = "특정 계좌의 거래 내역 확인", description = "거래 내역을 확인합니다.")
    @PostMapping("/history")
    public ResponseEntity<APIResponse> getHistory(
            @RequestBody RemittanceRequestDto.History dto
    ) {

        return ResponseEntity.ok(accountTransferHistoryService.getUserHistory(dto));
    }
}