package com.eum.bank.exception;

import com.eum.bank.common.ErrorResponse;
import com.eum.bank.common.enums.ErrorCode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import feign.FeignException;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.io.IOException;
import java.util.Iterator;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }/**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param e HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param e NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param ex IOException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * com.google.gson 내에 Exception 발생하는 경우
     *
     * @param ex JsonParseException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex) {
        log.error("handleJsonParseExceptionException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.JSON_PARSE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    /**
     * [Exception] 잘못된 인수로 요청 한 경우
     *
     * @param ex IllegalArgumentException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("IllegalArgumentException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_PARAMETER, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 다른 MSA 내 서비스와의 소통에서 오류가 발생한 경우. (현재 API 통신하는 서비스: 햇살 서비스)
     * HttpStatus 가 2xx 이 아닌 경우 FeignException 이 발생한다.
     *
     * @param ex FeignException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<ErrorResponse> feignException(FeignException ex) {

        /*
        API 호출 시 전송한 값을 추출하는 메서드.
        @param ex에서 ex.request 인 Request 객체의 toString() 내용을 공식 문서에서 일부 추출.
         */
        StringBuilder builder = new StringBuilder();
        Iterator var2 = ex.request().headers().keySet().iterator();

        while(var2.hasNext()) {
            String field = (String)var2.next();
            Iterator var4 = Util.valuesOrEmpty(ex.request().headers(), field).iterator();

            while(var4.hasNext()) {
                String value = (String)var4.next();
                builder.append(field).append(": ").append(value).append('\n');
            }
        }

        /*
        // TODO: Body 내용을 String화
        if (ex.request().body() != null) {
            builder.append('\n').append(Arrays.toString(ex.request().body()));
            log.error("리퀘스트 바디 널아님");
        }else{
            log.error("리퀘스트 바디 널임");
        }
         */

        String header = builder.toString();
        log.error("feignException 발생.\n오류 메시지: {},\n입력값: {}", ex.getMessage(), header);

        String reasonFromHaetsal = ex.getMessage().split("reason\":\"")[1].split("\"}]")[0];
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,
                String.format("햇살 서비스와의 통신에 문제가 있습니다. 이유: %s", reasonFromHaetsal));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * [Exception] block된 계좌일 경우
     * @param ex BlockAccountException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BlockAccountException.class)
    protected ResponseEntity<ErrorResponse> BlockAccountException(BlockAccountException ex) {
        log.error("BlockAccountException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BLOCK_ACCOUNT, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * [Exception] 비밀번호가 틀렸을 경우
     * @param ex WrongPasswordException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(WrongPasswordException.class)
    protected ResponseEntity<ErrorResponse> WrongPasswordException(WrongPasswordException ex) {
        log.error("WrongPasswordException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_PASSWORD, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * [Exception] 금액 부족
     * @param ex InsufficientAmountException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(InsufficientAmountException.class)
    protected ResponseEntity<ErrorResponse> InsufficientAmountException(InsufficientAmountException ex) {
        log.error("InsufficientAmountException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INSUFFICIENT_AMOUNT, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
    }

    /**
     * [Exception] 거래의 상태가 유효하지 않음
     * @param ex InvalidStatusException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(InvalidStatusException.class)
    protected ResponseEntity<ErrorResponse> InvalidDealStatusException(InvalidStatusException ex) {
        log.error("InvalidDealStatusException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_DEAL_STATUS, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 올바르지 않은 QR 정보
     * 유효시간이 지나거나 검증에 실패 시 사용된다.
     * @param ex InvalidQRExceptionHandler
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(InvalidQRExceptionHandler.class)
    protected ResponseEntity<ErrorResponse> InvalidQRException(InvalidQRExceptionHandler ex){
        log.error("InvalidQRException. 올바르지 않은 QR 정보입니다.", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_QR_CODE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ==================================================================================================================

    /**
     * [Exception] 모든 Exception 경우 발생
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Exception", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
