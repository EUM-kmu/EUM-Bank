package com.eum.bank.timeBank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.timeBank.service.TimeBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/timebank-service/api")
@Tag(name = "타임뱅크" ,description = "타임뱅크용 api")
public class TimeBankAccountController {

    private final TimeBankService timeBankService;

    @Operation(summary = "현재 잔액", description = "현재 잔액을 확인합니다.")
    @GetMapping("/account")
    public ResponseEntity<APIResponse<AccountResponseDTO.AccountInfo>> create(
            @RequestHeader String userId
    ) {

        APIResponse<AccountResponseDTO.AccountInfo> response = timeBankService.currentAccount(userId);
        return ResponseEntity.ok(response);
    }
}
