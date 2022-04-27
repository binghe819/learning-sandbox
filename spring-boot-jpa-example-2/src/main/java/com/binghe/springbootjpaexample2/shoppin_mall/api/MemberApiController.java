package com.binghe.springbootjpaexample2.shoppin_mall.api;

import com.binghe.springbootjpaexample2.shoppin_mall.application.MemberService;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Address;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 도메인 객체를 표현계층까지 노출하고있는 BAD Practice
     * 1. 도메인 객체가 컨트롤러에 노출된다.
     * 2. 요청별 어떤 값까지 필요한지 파악하기 어렵다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.register(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 도메인 객체를 컨트롤러에 노출하지 않는 BEST Practice
     * -> 애플리케이션 계층에서도 DTO를 만들어서 도메인 객체를 아예 노출하지 않는 것이 가장 좋다. (여기선 편의를 위해 Member를 생성하여 애플리케이션 계층에 전달)
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member(
                request.getName(),
                new Address(request.getCity(), request.getStreet(), request.getZipcode())
        );
        Long id = memberService.register(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findById(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /**
     * BAD Practice
     */
    @GetMapping("/api/v1/members/{id}")
    public List<Member> find(@PathVariable("id") Long id) {
        return memberService.findMembers();
    }

    /**
     * Best Practice
     */
    @GetMapping("/api/v2/members")
    public Result<List<MemberDto>> memberV2() {
        List<MemberDto> members = memberService
                .findMembers()
                .stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());
        return new Result(members.size(), members);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
        private String city;
        private String street;
        private String zipcode;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
