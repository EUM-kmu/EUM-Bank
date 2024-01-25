package com.eum.bank.domain.deal.entity;

import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Deal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 송신자 계좌
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account senderAccount;

    // 상태
    @Column(name = "status", nullable = false)
    private String status;

    // 예치금
    @Column(name = "deposit", nullable = false)
    private Long deposit;

    // 인원수
    @Column(name = "number_of_people", nullable = false)
    private Long numberOfPeople;

    // 게시글 ID
    @Column(name = "post_id", nullable = false)
    private Long postId;
}
