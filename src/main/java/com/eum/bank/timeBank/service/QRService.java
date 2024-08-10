package com.eum.bank.timeBank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.exception.InvalidQRExceptionHandler;
import com.eum.bank.repository.AccountRepository;
import com.eum.bank.timeBank.client.HaetsalClient;
import com.eum.bank.timeBank.client.HaetsalResponseDto;
import com.eum.bank.timeBank.controller.dto.request.QRRequestDto;
import com.eum.bank.timeBank.controller.dto.response.QRResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QRService {

    private final AccountRepository accountRepository;
    private final HaetsalClient haetsalClient;

    // secret key
    @Value("${app.qr.key}")
    private String SECRET_KEY;

    // hash 알고리즘 규격
    @Value("${app.qr.algorithm}")
    private String ALGORITHM;

    public QRResponseDto.QRCode createQRCode(String userId, String accountId) throws NoSuchAlgorithmException, InvalidKeyException {

        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Mac hasher = Mac.getInstance(ALGORITHM);
        hasher.init(secretKey);

        // 한국 시간대 설정
        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        // ISO 8601 형식으로 시간 포맷
        String formattedTime = nowInKorea.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String data = userId + "%" + accountId + "%" + formattedTime;

        byte[] hash = hasher.doFinal(data.getBytes());
        String hashed = HexUtils.toHexString(hash);

        return QRResponseDto.QRCode.from(hashed, data);

    }

    // TODO: GlobalExceptionHandler 에 NoSuchAlgorithmException, InvalidKeyException 도 추가하기
    public APIResponse<QRResponseDto.ScannedData> scanQRCode(QRRequestDto.QRCodeWithSenderInfo dto) throws NoSuchAlgorithmException, InvalidKeyException, FeignException {

        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Mac hasher = Mac.getInstance(ALGORITHM);
        hasher.init(secretKey);

        byte[] hash = hasher.doFinal(dto.getUserInfo().getBytes());
        String hashed = HexUtils.toHexString(hash);

        if (Objects.equals(hashed, dto.getHmac())) {

            String[] parts = dto.getUserInfo().split("%");
            String ReceiverUserId = parts[0];
            String ReceiverAccountId = parts[1];
            String createdAt = parts[2];

            if(!isValid(createdAt)){
                throw new InvalidQRExceptionHandler("유효시간이 지난 QR 코드 입니다.");
            }

            HaetsalResponseDto. Profile userInfo = haetsalClient.getProfile(ReceiverUserId).getData();

            Long senderBalance= accountRepository.findByAccountNumber(dto.getSenderAccountId()).get().getAvailableBudget();

            QRResponseDto.ScannedData response = QRResponseDto.ScannedData.from(ReceiverAccountId, userInfo, senderBalance);

            return APIResponse.of(SuccessCode.SELECT_SUCCESS, response);

        } else {
            throw new InvalidQRExceptionHandler("QR 코드 인증에 실패했습니다");
        }
    }

    private boolean isValid(String createdAt) {

        // 문자열을 ZonedDateTime 객체로 파싱
        ZonedDateTime parsedTime = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // 현재 한국 시간대의 시간
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        // 두 시간 사이의 차이 계산
        Duration duration = Duration.between(parsedTime, now);

        System.out.println(duration);

        // 차이가 2분 이상인지 확인
        return duration.toMinutes() <= 2;
    }
}
