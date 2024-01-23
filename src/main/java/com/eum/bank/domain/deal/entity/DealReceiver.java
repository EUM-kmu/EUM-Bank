package com.eum.bank.domain.deal.entity;

import com.eum.bank.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 거래 ID
    @ManyToOne
    @JoinColumn(name = "deal_id")
    private Deal deal;

    // 수신자 계좌
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account receiverAccount;
}
