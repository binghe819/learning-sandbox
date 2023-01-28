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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * CompletableFuture를 이용한 비동기 방식으로 회원 정보 및 잔고를 조회하는 Service
 * - 비동기로 전환했을 뿐, 각 상황별 예외 처리는 하지 않음.
 */
@RequiredArgsConstructor
@Service
public class MemberQueryServiceV2 {

    private final ExecutorService asyncExecutorService;

    private final MemberRepository memberRepository;
    private final CheckBalance dossBankCheckBalanceV2;
    private final CheckBalance kakaokBankCheckBalanceV2;
    private final CheckBalance shinhenBankCheckBalanceV2;

    @StopWatch
    public CheckMemberBalanceResponse checkMemberBalances(Long memberId) {
        CompletableFuture<Member> memberCompletableFuture = CompletableFuture
                .supplyAsync(() -> memberRepository.findById(memberId).orElse(new Member("anonymous", 27)), asyncExecutorService);
        CompletableFuture<CheckBalanceResult> dossBalanceCompletableFuture = CompletableFuture
                .supplyAsync(() -> dossBankCheckBalanceV2.checkBalance(memberId), asyncExecutorService);
        CompletableFuture<CheckBalanceResult> kakaokBalanceCompletableFuture = CompletableFuture
                .supplyAsync(() -> kakaokBankCheckBalanceV2.checkBalance(memberId), asyncExecutorService);
        CompletableFuture<CheckBalanceResult> shinhenBalanceCompletableFuture = CompletableFuture
                .supplyAsync(() -> shinhenBankCheckBalanceV2.checkBalance(memberId), asyncExecutorService);

        CompletableFuture.allOf(
                memberCompletableFuture,
                dossBalanceCompletableFuture,
                kakaokBalanceCompletableFuture,
                shinhenBalanceCompletableFuture
        ).exceptionally(ex -> {
            throw new RuntimeException();
        }).join();

        Member member = memberCompletableFuture.join();
        CheckBalanceResult dossBalance = dossBalanceCompletableFuture.join();
        CheckBalanceResult kakaokBalance = kakaokBalanceCompletableFuture.join();
        CheckBalanceResult shinhenBalance = shinhenBalanceCompletableFuture.join();
        return CheckMemberBalanceResponse.builder()
                .name(memberCompletableFuture.join().getName())
                .age(member.getAge())
                .checkBalances(List.of(dossBalance, kakaokBalance, shinhenBalance))
                .build();
    }
}
