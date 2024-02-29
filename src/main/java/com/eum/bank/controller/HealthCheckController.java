package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.enums.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping()
    public ResponseEntity<APIResponse<Long>> healthCheck(){
        Long result = 1L;
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS, result));
    }

}
