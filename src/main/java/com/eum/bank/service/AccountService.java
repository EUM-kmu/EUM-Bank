package com.eum.bank.service;

import com.eum.bank.common.APIResponse;
import com.eum.bank.common.dto.response.AccountResponseDTO;
import com.eum.bank.common.enums.SuccessCode;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    public APIResponse<?> createAccount(Long password) {

        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        }while (
                validateAccountNumber(accountNumber)
        );

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .password(passwordEncoder.encode(password.toString()))
                .totalBudget(0L)
                .availableBudget(0L)
                .build();

        accountRepository.save(account);

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, AccountResponseDTO.Create.builder()
                .accountNumber(account.getAccountNumber())
                .build());
    }

    public String generateAccountNumber() {
        Random random = new Random();
        StringBuilder uniqueNumber = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int digit = random.nextInt(10);
            uniqueNumber.append(digit);
        }

        if( validateAccountNumber(uniqueNumber.toString()) )
            generateAccountNumber();

        return uniqueNumber.toString();
    }
    public Boolean validateAccountNumber(String accountNumber) {
        if (accountNumber.length() != 12) {
            return false;
        }

        try {
            Long.parseLong(accountNumber);
            accountRepository.findByAccountNumber(accountNumber);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }



}
