package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.common.dto.response.DealResponseDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.service.AccountService;
import com.eum.bank.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래", description = "거래 API")
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;

    @Operation(summary = "거래 생성", description = "거래를 생성합니다.")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DealRequestDTO.createDeal create) {
        APIResponse<?> response = dealService.createDeal(create);
        
        return ResponseEntity.ok(response);
    }

    // 거래 성사
    @Operation(summary = "거래 성사", description = "거래를 성사합니다. 거래 성사란 모집인원이 모두 모이거나 제시한 사람이 요청인이 완료를 누른 경우 입니다.")
    @PostMapping("/success")
    public ResponseEntity<?> success(@RequestBody DealRequestDTO.completeDeal success) {
        return ResponseEntity.ok(dealService.completeDeal(success));
    }

    // 거래 수정
    @Operation(summary = "거래 수정", description = "거래를 수정합니다.")
    @PatchMapping
    public ResponseEntity<?> update(@RequestBody DealRequestDTO.updateDeal update) {
        return ResponseEntity.ok(dealService.updateDeal(update));
    }

    // 거래 취소
    @Operation(summary = "거래 취소", description = "거래를 취소합니다.")
    @DeleteMapping
    public ResponseEntity<?> cancel(@RequestBody DealRequestDTO.cancelDeal cancel) {
        return ResponseEntity.ok(dealService.cancelDeal(cancel));
    }

    // 거래 수행
    @Operation(summary = "거래 수행", description = "거래를 수행합니다. 일괄 송금하기")
    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody DealRequestDTO.executeDeal execute) {
        return ResponseEntity.ok(dealService.executeDeal(execute));
    }
}
