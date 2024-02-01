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
        APIResponse<?> response = dealService.createDeal(create);
        
        return ResponseEntity.ok(response);
    }

    // 거래 성사
    @PostMapping("/success")
    public ResponseEntity<?> success(@RequestBody DealRequestDTO.completeDeal success) {
        return ResponseEntity.ok(dealService.completeDeal(success));
    }

    // 거래 수정
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody DealRequestDTO.updateDeal update) {
        return ResponseEntity.ok(dealService.updateDeal(update));
    }

    // 거래 취소
    @PostMapping("/cancel")
    public ResponseEntity<?> cancel(@RequestBody DealRequestDTO.cancelDeal cancel) {
        return ResponseEntity.ok(dealService.cancelDeal(cancel));
    }

    // 거래 수행
    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody DealRequestDTO.executeDeal execute) {
        return ResponseEntity.ok(dealService.executeDeal(execute));
    }
}
