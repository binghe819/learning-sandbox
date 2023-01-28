package com.binghe.threadperrequestmodelperformanceimprovesynchronous.application;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.Member;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.MemberRepository;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.StopWatch;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation.CheckMemberBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 동기/블로킹 방식으로 회원 정보 및 잔고를 조회하는 Service
 */
@RequiredArgsConstructor
@Service
public class MemberQueryServiceV1 {

    private final MemberRepository memberRepository;
    private final CheckBalance dossBankCheckBalanceV1;
    private final CheckBalance kakaokBankCheckBalanceV2;
    private final CheckBalance shinhenBankCheckBalanceV3;

    @StopWatch
    public CheckMemberBalanceResponse checkMemberBalances(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(new Member("anonymous", 27));
        CheckBalanceResult dossBalance = dossBankCheckBalanceV1.checkBalance(memberId);
        CheckBalanceResult kakaokBalance = kakaokBankCheckBalanceV2.checkBalance(memberId);
        CheckBalanceResult shinhenBalance = shinhenBankCheckBalanceV3.checkBalance(memberId);
        return CheckMemberBalanceResponse.builder()
                .name(member.getName())
                .age(member.getAge())
                .checkBalances(List.of(dossBalance, kakaokBalance, shinhenBalance))
                .build();
    }
}
