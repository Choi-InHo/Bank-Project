package com.example.BankProject.loan.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

public class Balance {
    //얼마를 집행 받아서 얼마를 갚았고 현재 얼마 남았는지에 대한 내용
    // 얼마를 집행 받아서 얼마를 갚았고 현재 얼마 남았는지에 대한 내용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long balanceId;

    @Column(columnDefinition = "bigint NOT NULL COMMENT '신청 ID'")
    private Long applicationId;

    @Column(columnDefinition = "decimal(15,2) NOT NULL COMMENT '잔여 대출 금액'")
    private BigDecimal balance;
}
