package com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.application.MemberQueryServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberQueryController {

    private final MemberQueryServiceV1 memberQueryServiceV1;

    public MemberQueryController(MemberQueryServiceV1 memberQueryServiceV1) {
        this.memberQueryServiceV1 = memberQueryServiceV1;
    }

    @GetMapping("/V1/members/{id}")
    public ResponseEntity<CheckMemberBalanceResponse> findById(@PathVariable Long id) {
        CheckMemberBalanceResponse result = memberQueryServiceV1.checkMemberBalances(id);
        return ResponseEntity.ok(result);
    }
}
