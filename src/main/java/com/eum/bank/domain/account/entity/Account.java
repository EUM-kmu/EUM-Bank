package com.eum.bank.domain.account.entity;

import com.eum.bank.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account extends BaseEntity {

    // 계좌 번호
    @Id
    @Column(name = "account_number", length = 12, nullable = false)
    private String accountNumber;

    // 비밀번호
    @Column(name = "password", nullable = false)
    private String password;

    // 계좌 총 잔액
    @Column(name = "total_budget", nullable = false)
    private Long totalBudget;

    // 계좌 사용 가능 잔액
    @Column(name = "available_budget", nullable = false)
    private Long availableBudget;

    // 블락 여부
    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    public static Account initializeAccount(String accountNumber, String password) {
        return Account.builder()
                .accountNumber(accountNumber)
                .password(password)
                .totalBudget(300L)
                .availableBudget(300L)
                .isBlocked(false)
                .build();
    }

}
