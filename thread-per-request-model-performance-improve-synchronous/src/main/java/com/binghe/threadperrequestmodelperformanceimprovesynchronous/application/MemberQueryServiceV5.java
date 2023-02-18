package com.binghe.threadperrequestmodelperformanceimprovesynchronous.application;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.Member;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.MemberRepository;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.StopWatch;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation.CheckMemberBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * CompletableFuture를 이용한 비동기 방식으로 회원 정보 및 잔고를 조회하는 Service
 * - 비동기 Task에 대한 Timeout 설정. TODO
 */
@RequiredArgsConstructor
@Service
public class MemberQueryServiceV4 {

    private final ExecutorService asyncExecutorService;

    private final MemberRepository memberRepository;
    private final CheckBalance dossBankCheckBalanceV4;
    private final CheckBalance kakaokBankCheckBalanceV4;
    private final CheckBalance shinhenBankCheckBalanceV4;

    @StopWatch
    public CheckMemberBalanceResponse checkMemberBalances(Long memberId) {
        // Member를 찾는 과정에서 예외 발생시 HTTP 요청 자체가 예외를 던져야한다.
        // 이때 아무 예외 핸들링을 해주지않으면, 해당 비동기 Task에 대한 get() 혹은 join()을 호출한 스레드에 예외가 전파된다. (중요!)
        CompletableFuture<Member> memberCompletableFuture = CompletableFuture
                .supplyAsync(
                        () -> memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 회원입니다.")),
                        asyncExecutorService
                );

        CompletableFuture<CheckBalanceResult> dossBalanceCompletableFuture = CompletableFuture
                .supplyAsync(() -> dossBankCheckBalanceV4.checkBalance(memberId), asyncExecutorService)
                .exceptionally(ex -> CheckBalanceResult.EMPTY);

        // 비동기 Task 실행중 예외 발생할 경우 기본값으로 처리하도록 설정.
        CompletableFuture<CheckBalanceResult> kakaokBalanceCompletableFuture = CompletableFuture
                .supplyAsync(() -> kakaokBankCheckBalanceV4.checkBalance(memberId), asyncExecutorService)
                .exceptionally(ex -> CheckBalanceResult.EMPTY);

        CompletableFuture<CheckBalanceResult> shinhenBalanceCompletableFuture = CompletableFuture
                .supplyAsync(() -> shinhenBankCheckBalanceV4.checkBalance(memberId), asyncExecutorService)
                .exceptionally(ex -> CheckBalanceResult.EMPTY);


        List<CompletableFuture<?>> futures = Arrays
                .asList(memberCompletableFuture, dossBalanceCompletableFuture, kakaokBalanceCompletableFuture, shinhenBalanceCompletableFuture);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);

        // memberCompletableFuture에서 던져진 예외가 여기에서 호출자 스레드까지 전파된다.
        // 그리고 HTTP 요청은 200이 아닌 다른 예외를 반환하게된다.
        CompletableFuture.allOf(futuresArray).join();

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
