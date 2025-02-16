package com.binghe.presentation;

import com.binghe.domain.Member;
import com.binghe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody Member member) {
        Long id = memberService.saveMember(member);
        return ResponseEntity.ok(String.valueOf(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMembers() {
        memberService.deleteAllMembers();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getMemberCount() {
        return ResponseEntity.ok(memberService.getMemberCount());
    }

    @PutMapping
    public ResponseEntity<Void> updateMember(@RequestBody Member member) {
        memberService.updateMember(member);
        return ResponseEntity.ok().build();
    }
}

