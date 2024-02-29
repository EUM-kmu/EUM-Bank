package com.eum.bank.domain.deal.entity;

import com.eum.bank.common.dto.request.DealRequestDTO;
import com.eum.bank.domain.account.entity.Account;
import com.eum.bank.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.eum.bank.common.Constant.BEFORE_DEAL;
import static com.eum.bank.common.Constant.FREE_TYPE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Deal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // 최대 인원수
    @Column(name = "max_people_num", nullable = false)
    private Long maxPeopleNum;

    // 실제 모집인원수
    @Column(name = "real_people_num", nullable = false)
    private Long realPeopleNum;

    // 게시글 ID
    @Column(name = "post_id", nullable = false)
    private Long postId;

    public static Deal initializeDeal(Account account,DealRequestDTO.CreateDeal createDeal) {
        return Deal.builder()
                .senderAccount(account)
                .status(BEFORE_DEAL)
                .deposit(createDeal.getDeposit())
                .maxPeopleNum(createDeal.getMaxPeople())
                .realPeopleNum(0L)
                .postId(createDeal.getPostId())
                .build();
    }
}
