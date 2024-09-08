package com.binghe

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : ReactiveCrudRepository<Member, Long>
