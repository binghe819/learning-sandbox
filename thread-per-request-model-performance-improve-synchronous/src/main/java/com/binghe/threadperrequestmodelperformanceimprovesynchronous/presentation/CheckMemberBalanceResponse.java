package com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckMemberBalanceResponse {

    private String name;
    private int age;
    private List<CheckBalanceResult> checkBalances;
}
