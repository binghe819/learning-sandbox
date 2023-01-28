package com.binghe.threadperrequestmodelperformanceimprovesynchronous.application;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.Member;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.MemberRepository;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.infrastructure.timecount.StopWatch;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation.CheckMemberBalanceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 동기/블로킹 방식으로 회원 정보 및 잔고를 조회하는 Service
 */

@Service
public class MemberQueryServiceV1 {

    private final MemberRepository memberRepository;
    private final CheckBalance dossBankCheckBalanceV1;
    private final CheckBalance kakaokBankCheckBalanceV1;
    private final CheckBalance shinhenBankCheckBalanceV1;

    public MemberQueryServiceV1(MemberRepository memberRepository, CheckBalance dossBankCheckBalanceV1, CheckBalance kakaokBankCheckBalanceV1, CheckBalance shinhenBankCheckBalanceV1) {
        this.memberRepository = memberRepository;
        this.dossBankCheckBalanceV1 = dossBankCheckBalanceV1;
        this.kakaokBankCheckBalanceV1 = kakaokBankCheckBalanceV1;
        this.shinhenBankCheckBalanceV1 = shinhenBankCheckBalanceV1;
    }

    @StopWatch
    public CheckMemberBalanceResponse checkMemberBalances(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(new Member("anonymous", 27));
        CheckBalanceResult dossBalance = dossBankCheckBalanceV1.checkBalance(memberId);
        CheckBalanceResult kakaokBalance = kakaokBankCheckBalanceV1.checkBalance(memberId);
        CheckBalanceResult shinhenBalance = shinhenBankCheckBalanceV1.checkBalance(memberId);
        return CheckMemberBalanceResponse.builder()
                .name(member.getName())
                .age(member.getAge())
                .checkBalances(List.of(dossBalance, kakaokBalance, shinhenBalance))
                .build();
    }
}
