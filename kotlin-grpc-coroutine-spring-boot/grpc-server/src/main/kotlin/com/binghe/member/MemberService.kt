package com.binghe

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MemberService @Autowired constructor(private val memberRepository: MemberRepository) {

    suspend fun findById(id: Long): Member? {
        return memberRepository.findById(id)
            .awaitSingleOrNull()
    }
}