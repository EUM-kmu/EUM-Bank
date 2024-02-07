package com.eum.bank;

import com.eum.bank.repository.AccountRepository;
import com.eum.bank.service.AccountService;
import com.eum.bank.service.AccountTransferHistoryService;
import com.eum.bank.service.TotalTransferHistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class BankUnitTest {

    @Test
    public void test_validateAccountNumber_method() {
        // given
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AccountTransferHistoryService accountTransferHistoryService = Mockito.mock(AccountTransferHistoryService.class);
        TotalTransferHistoryService totalTransferHistoryService = Mockito.mock(TotalTransferHistoryService.class);

        AccountService accountService = new AccountService(accountRepository, passwordEncoder, accountTransferHistoryService, totalTransferHistoryService);

        // when
        boolean result = accountService.validateAccountNumber("123456789012");

        // then
        assertTrue(result);
    }

    @Test
    public void test_false_validateAccountNumber_method() {
        // given
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AccountTransferHistoryService accountTransferHistoryService = Mockito.mock(AccountTransferHistoryService.class);
        TotalTransferHistoryService totalTransferHistoryService = Mockito.mock(TotalTransferHistoryService.class);

        AccountService accountService = new AccountService(accountRepository, passwordEncoder, accountTransferHistoryService, totalTransferHistoryService);

        // when
        boolean result = accountService.validateAccountNumber("1234567890123");

        // then
        assertFalse(result);
    }

    @Test
    public void test_generateAccountNumber_method() {
        // given
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AccountTransferHistoryService accountTransferHistoryService = Mockito.mock(AccountTransferHistoryService.class);
        TotalTransferHistoryService totalTransferHistoryService = Mockito.mock(TotalTransferHistoryService.class);

        AccountService accountService = new AccountService(accountRepository, passwordEncoder, accountTransferHistoryService, totalTransferHistoryService);

        // when
        String accountNumber = accountService.generateAccountNumber();

        // then
        assertEquals( 12, accountNumber.length());
    }

    @Test
    public void test_validateAccount() {
        // given
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AccountTransferHistoryService accountTransferHistoryService = Mockito.mock(AccountTransferHistoryService.class);
        TotalTransferHistoryService totalTransferHistoryService = Mockito.mock(TotalTransferHistoryService.class);

        AccountService accountService = new AccountService(accountRepository, passwordEncoder, accountTransferHistoryService, totalTransferHistoryService);

        // when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.validateAccount("123456789012");
        });

        // then
        assertEquals("Invalid account number : 123456789012", exception.getMessage());
    }

    @Test
    public void test_invalid_matchAccount() {
        // given
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AccountTransferHistoryService accountTransferHistoryService = Mockito.mock(AccountTransferHistoryService.class);
        TotalTransferHistoryService totalTransferHistoryService = Mockito.mock(TotalTransferHistoryService.class);

        AccountService accountService = new AccountService(accountRepository, passwordEncoder, accountTransferHistoryService, totalTransferHistoryService);

        // when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.matchAccountPassword("123456789012", "password");
        });

        // then
        assertEquals("Invalid account number : 123456789012", exception.getMessage());
    }

}
