package com.binghe

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(private val memberService: MemberService) {

    @GetMapping("/member/{id}")
    suspend fun findById(@PathVariable id: Long): Member? =
        memberService.findById(id)
}
