package com.binghe;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
}
