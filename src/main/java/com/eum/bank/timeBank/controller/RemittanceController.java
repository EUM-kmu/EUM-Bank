package com.eum.bank.timeBank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.AccountRequestDTO;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.common.enums.ErrorCode;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.service.AccountService;
import com.eum.bank.service.AccountTransferHistoryService;
import com.eum.bank.timeBank.controller.dto.request.RemittanceRequestDto;
import com.eum.bank.timeBank.service.ValidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timebank-service/api/remittance")
@Tag(name = "타임뱅크" ,description = "타임뱅크용 api")
public class RemittanceController {

    private final AccountService accountService;
    private final AccountTransferHistoryService accountTransferHistoryService;
    private final ValidateService validateService;

    // 송금
    @Operation(summary = "QR 송금", description = "QR 송금을 합니다.")
    @PostMapping("/qr")
    public ResponseEntity<APIResponse<TotalTransferHistoryResponseDTO.GetTotalTransferHistory>> QRRemittance(
            @Schema(description = "송금 요청", required = true)
            @RequestHeader String userId,
            @RequestBody RemittanceRequestDto.QRRemittance dto
    ) throws NoSuchAlgorithmException, InvalidKeyException {

        boolean isValid = validateService.hmacRemittance(dto);

        if(!isValid){
            return ResponseEntity.ok(APIResponse.of(ErrorCode.INVALID_DEAL_STATUS, "인증되지 않은 송금 요청입니다."));
        }

        AccountRequestDTO.Transfer transfer = AccountRequestDTO.Transfer.fromHMAC(dto.getRemittanceInfo());

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