package com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @StopWatch
    @Query(value = "SELECT m FROM Member m WHERE m.id = :id")
    Optional<Member> findById(Long id);
}
