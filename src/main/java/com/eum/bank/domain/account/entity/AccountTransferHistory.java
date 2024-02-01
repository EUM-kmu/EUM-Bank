package com.eum.bank.domain.account.entity;

import com.eum.bank.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTransferHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 주인 계좌
    @ManyToOne
    @JoinColumn(name = "owner_account_number")
    private Account ownerAccount;

    // 상대 계좌
    @ManyToOne
    @JoinColumn(name = "oppenent_account_number")
    private Account oppenentAccount;

    // 거래 금액
    @Column(name = "transfer_amount", nullable = false)
    private Long transferAmount;

    // 거래 유형
    // a: 자유송금
    // b: 거래 송금
    @Column(name = "transfer_type", nullable = false)
    private String transferType;

    // 거래 후 잔액
    @Column(name = "budget_after_transfer", nullable = false)
    private Long budgetAfterTransfer;

    // 거래 메모
    @Column(name = "memo", nullable = false)
    private String memo;
}
