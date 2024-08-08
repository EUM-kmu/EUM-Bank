package com.eum.bank.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HMAC 원본메시지 형식이 유효한지 검사하는 커스텀 어노테이션
 * 원본메시지 내 % 개수(필드개수 - 1)를 확인한다.
 */
@Constraint(validatedBy = MessageOfHMACValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMessageOfHMAC {
    String message() default "요구하는 형식이 아닙니다. 다시 확인해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * 한 줄로 적힌 원본 메시지 내에 들어있는 필드의 개수.
     * (예)"<userId>%<accountId>%<createdAt>" 와 같은 형식이라면, field 값은 3이다.
     */
    int field();
}
