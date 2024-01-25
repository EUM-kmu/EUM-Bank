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
public class TotalTransferHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 송신자 계좌
    @ManyToOne
    @JoinColumn(name = "sender_account_number")
    private Account senderAccount;

    // 수신자 계좌
    @ManyToOne
    @JoinColumn(name = "receiver_account_number")
    private Account receiverAccount;

    // 거래 금액
    @Column(name = "transfer_amount", nullable = false)
    private Long transferAmount;

    // 거래 유형
    @Column(name = "transfer_type", nullable = false)
    private String transferType;

}
