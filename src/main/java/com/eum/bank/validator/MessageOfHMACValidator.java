package com.eum.bank.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @ValidMessageOfHMAC 의 Validator
 * field의 수와 field를 나누는 %의 개수의 차이가 1인지 검증한다.
 */
public class MessageOfHMACValidator implements ConstraintValidator<ValidMessageOfHMAC, String> {

    private int field;

    @Override
    public void initialize(ValidMessageOfHMAC constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    /**
     * field의 수와 field를 나누는 %의 개수의 차이가 1인지 검증한다.
     */
    @Override
    public boolean isValid(String message, ConstraintValidatorContext constraintValidatorContext) {
        if(message == null || message.isEmpty()){
            return false;
        }

        int count = message.length() - message.replace("%", "").length();
        return count == field - 1;
    }
}
