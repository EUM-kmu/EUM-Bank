package com.eum.bank.timeBank.controller;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.timeBank.controller.dto.request.QRRequestDto;
import com.eum.bank.timeBank.controller.dto.response.QRResponseDto;
import com.eum.bank.timeBank.service.QRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timebank-service/api/qr")
@Tag(name = "타임뱅크" ,description = "타임뱅크용 api")
public class QRController {

    private final QRService qrService;

    @Operation(summary = "QR 생성", description = "QR 코드를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<APIResponse<QRResponseDto.QRCode>> createQRCode(
            @RequestHeader String userId,
            @RequestBody @Valid QRRequestDto.BaseInfo dto
    ) throws NoSuchAlgorithmException, InvalidKeyException {

        QRResponseDto.QRCode response = qrService.createQRCode(userId, dto.getAccountId());
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, response));
    }


    @Operation(summary = "QR 스캔", description = "QR 코드에서 정보를 추출합니다.")
    @PostMapping("/scan")
    public ResponseEntity<APIResponse> scanQRCode(
            @RequestBody @Valid QRRequestDto.QRCodeWithSenderInfo dto
    ) throws Exception {

        APIResponse response = qrService.scanQRCode(dto);

        return ResponseEntity.ok(response);
    }

}
