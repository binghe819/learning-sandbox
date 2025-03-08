package com.binghe.service;

import com.binghe.domain.Member;
import com.binghe.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @Test
    void saveMember_ShouldPersistAndReturnMemberId() {
        Member member = new Member("binghe", "binghe address", "binghe description");

        Long memberId = memberService.saveMember(member);
        Optional<Member> savedMember = memberService.getMemberById(memberId);

        assertThat(savedMember).isPresent();
        assertThat(savedMember.get().getId()).isEqualTo(1L);
        assertThat(savedMember.get().getName()).isEqualTo("binghe");
        assertThat(savedMember.get().getAddress()).isEqualTo("binghe address");
        assertThat(savedMember.get().getDescription()).isEqualTo("binghe description");
    }

    @Test
    void getAllMembers_ShouldReturnList() {
        Member member1 = new Member("Binghe 1", "Binghe 1 address", "Binghe 1 description");
        Member member2 = new Member("Binghe 2", "Binghe 2 address", "Binghe 2 description");

        memberService.saveMember(member1);
        memberService.saveMember(member2);

        List<Member> members = memberService.getAllMembers();

        assertThat(members).hasSize(2);
        assertThat(members.get(0).getName()).isEqualTo(member1.getName());
        assertThat(members.get(0).getAddress()).isEqualTo(member1.getAddress());
        assertThat(members.get(0).getDescription()).isEqualTo(member1.getDescription());
        assertThat(members.get(1).getName()).isEqualTo(member2.getName());
        assertThat(members.get(1).getAddress()).isEqualTo(member2.getAddress());
        assertThat(members.get(1).getDescription()).isEqualTo(member2.getDescription());
    }

    @Test
    void deleteAllMembers_ShouldRemoveAllRecords() {
        memberService.saveMember(new Member("Binghe 1", "Binghe 1 address", "Binghe 1 for delete test"));
        memberService.saveMember(new Member("Binghe 2", "Binghe 2 address", "Binghe 2 for delete test"));

        memberService.deleteAllMembers();

        assertThat(memberService.getMemberCount()).isEqualTo(0);
    }

    @Test
    void getMemberCount_ShouldReturnCorrectCount() {
        memberService.saveMember(new Member("Binghe 1", "Binghe 1 address", "Binghe 1 for count test"));
        memberService.saveMember(new Member("Binghe 2", "Binghe 2 address", "Binghe 2 for count test"));

        int count = memberService.getMemberCount();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void updateMember_ShouldModifyExistingMember() {
        Member member = new Member("Binghe 1", "Binghe 1 address", "Binghe 1 for update test");
        Long savedMemberId = memberService.saveMember(member);

        Member needToUpdateMember = memberService.getMemberById(savedMemberId).orElse(null);
        needToUpdateMember.updateAddress("updated Binghe 1 address");
        memberService.updateMember(needToUpdateMember);

        Optional<Member> updatedMember = memberService.getMemberById(savedMemberId);
        assertThat(updatedMember).isPresent();
        assertThat(updatedMember.get().getAddress()).isEqualTo("updated Binghe 1 address");
    }
}
