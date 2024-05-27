package com.eum.bank.timeBank.service;

import com.eum.bank.timeBank.controller.dto.request.RemittanceRequestDto;
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
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ValidateService {

    // secret key
    @Value("${app.remittance.key}")
    private String SECRET_KEY;

    // hash 알고리즘 규격
    @Value("${app.remittance.algorithm}")
    private String ALGORITHM;


    public boolean hmacRemittance(RemittanceRequestDto.QRRemittance dto) throws NoSuchAlgorithmException, InvalidKeyException {
        String hmac = dto.getHmac();
        String data = dto.getRemittanceInfo();

        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Mac hasher = Mac.getInstance(ALGORITHM);
        hasher.init(secretKey);

        byte[] hash = hasher.doFinal(data.getBytes());
        String hashed = HexUtils.toHexString(hash);

        return Objects.equals(hashed, hmac);
    }
}
