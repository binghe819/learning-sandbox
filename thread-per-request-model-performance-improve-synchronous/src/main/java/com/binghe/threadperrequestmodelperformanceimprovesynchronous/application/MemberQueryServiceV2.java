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
 * CompletableFuture를 이용한 비동기 방식으로 회원 정보 및 잔고를 조회하는 Service
 */
@RequiredArgsConstructor
@Service
public class MemberQueryServiceV2 {

    private final MemberRepository memberRepository;
    private final CheckBalance dossBankCheckBalance;
    private final CheckBalance kakaokBankCheckBalance;
    private final CheckBalance shinhenBankCheckBalance;

    @StopWatch
    public CheckMemberBalanceResponse checkMemberBalances(Long memberId) {



        Member member = memberRepository.findById(memberId).orElse(new Member("anonymous", 27));
        CheckBalanceResult dossBalance = dossBankCheckBalance.checkBalance(memberId);
        CheckBalanceResult kakaokBalance = kakaokBankCheckBalance.checkBalance(memberId);
        CheckBalanceResult shinhenBalance = shinhenBankCheckBalance.checkBalance(memberId);
        return CheckMemberBalanceResponse.builder()
                .name(member.getName())
                .age(member.getAge())
                .checkBalances(List.of(dossBalance, kakaokBalance, shinhenBalance))
                .build();
    }
}
