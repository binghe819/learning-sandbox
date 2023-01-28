package com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.application.MemberQueryServiceV1;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.application.MemberQueryServiceV2;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.application.MemberQueryServiceV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberQueryController {

    private final MemberQueryServiceV1 memberQueryServiceV1;
    private final MemberQueryServiceV2 memberQueryServiceV2;
    private final MemberQueryServiceV3 memberQueryServiceV3;

    @GetMapping("/v1/members/{id}")
    public ResponseEntity<CheckMemberBalanceResponse> findById(@PathVariable Long id) {
        CheckMemberBalanceResponse result = memberQueryServiceV1.checkMemberBalances(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/v2/members/{id}")
    public ResponseEntity<CheckMemberBalanceResponse> findByIdV2(@PathVariable Long id) {
        CheckMemberBalanceResponse result = memberQueryServiceV2.checkMemberBalances(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/v3/members/{id}")
    public ResponseEntity<CheckMemberBalanceResponse> findByIdV3(@PathVariable Long id) {
        CheckMemberBalanceResponse result = memberQueryServiceV3.checkMemberBalances(id);
        return ResponseEntity.ok(result);
    }
}
