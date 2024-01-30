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
    // a: 거래 생성 후 거래 성사 전 (수신계좌가 안엮인 상태)
    // b: 거래 성사 후 (수신계좌가 엮인 상태)
    // c: 거래 취소 됨
    // d: 거래 수행 됨
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
