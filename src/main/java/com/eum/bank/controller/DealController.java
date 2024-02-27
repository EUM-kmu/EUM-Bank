package com.eum.bank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.ErrorResponse;
import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.common.dto.response.DealResponseDTO;
import com.eum.bank.common.dto.response.DealResponseDTO.createDeal;
import com.eum.bank.common.dto.response.TotalTransferHistoryResponseDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.service.AccountService;
import com.eum.bank.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "거래", description = "거래 API")
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;

    @Operation(summary = "거래 생성", description = "거래를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호 인증 에러 또는 거래상태 비적합",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "block된 계좌", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<APIResponse<DealResponseDTO.createDeal>> create(
            @Schema(description = "거래 생성 정보", required = true, implementation = DealRequestDTO.CreateDeal.class)
            @RequestBody DealRequestDTO.CreateDeal create) {
        APIResponse<DealResponseDTO.createDeal> response = dealService.createDeal(create);
        
        return ResponseEntity.ok(response);
    }

    // 거래 성사
    @Operation(summary = "거래 성사", description = "거래를 성사합니다. 거래 성사란 모집인원이 모두 모이거나 제시한 사람이 요청인이 완료를 누른 경우 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호 인증 에러 또는 거래상태 비적합",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "block된 계좌", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/success")
    public ResponseEntity<APIResponse<DealResponseDTO.createDeal>> success(
            @Schema(description = "거래 성사 정보", required = true, implementation = DealRequestDTO.CompleteDeal.class)
            @RequestBody DealRequestDTO.CompleteDeal success) {
        return ResponseEntity.status(200).body((dealService.completeDeal(success)));
    }

    // 거래 수정
    @Operation(summary = "거래 수정", description = "거래를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호 인증 에러 또는 거래상태 비적합",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "block된 계좌", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<APIResponse<DealResponseDTO.createDeal>> update(
            @Schema(description = "거래 수정 정보", required = true, implementation = DealRequestDTO.UpdateDeal.class)
            @RequestBody DealRequestDTO.UpdateDeal update) {
        return ResponseEntity.ok(dealService.updateDeal(update));
    }

    // 거래 취소
    @Operation(summary = "거래 취소", description = "거래를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호 인증 에러 또는 거래상태 비적합",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "block된 계좌", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<APIResponse<DealResponseDTO.createDeal>> cancel(
            @Schema(description = "거래 취소 정보", required = true, implementation = DealRequestDTO.CancelDeal.class)
            @RequestBody DealRequestDTO.CancelDeal cancel) {
        return ResponseEntity.ok(dealService.cancelDeal(cancel));
    }

    // 거래 수행
    @Operation(summary = "거래 수행", description = "거래를 수행합니다. 일괄 송금하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호 인증 에러 또는 거래상태 비적합",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "block된 계좌", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/execute")
    public ResponseEntity<APIResponse<List<TotalTransferHistoryResponseDTO.GetTotalTransferHistory>>> execute(
            @Schema(description = "거래 수행 정보", required = true, implementation = DealRequestDTO.ExecuteDeal.class)
            @RequestBody DealRequestDTO.ExecuteDeal execute) {
        return ResponseEntity.ok(dealService.executeDeal(execute));
    }
}
