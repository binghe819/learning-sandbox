package com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @StopWatch
    Optional<Member> findById(Long id);
}
