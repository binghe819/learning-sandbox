package com.binghe.domain;

import com.binghe.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testSaveAndFindById() {
        System.out.println("testSaveAndFindById");
        Member member = new Member("binghe", "binghe address", "binghe description");
        memberRepository.save(member);

        // 테스트 격리를 하지 않으면 다른 테스트 실행후 이 테스트 실행시 실패함.
        Member foundMember = memberRepository.findById(1L).orElse(null);
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo(member.getName());
        assertThat(foundMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(foundMember.getDescription()).isEqualTo(member.getDescription());

        System.out.println("testSaveAndFindById finished");
    }

    @Test
    public void testGetAll() {
        System.out.println("testGetAll");
        Member member1 = new Member("Binghe 1", "Binghe 1 address", "Binghe 1 description");
        Member member2 = new Member("Binghe 2", "Binghe 2 address", "Binghe 2 description");
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.getAll();
        assertThat(members).isNotEmpty();
        assertThat(members).hasSize(2);
        assertThat(members.get(0).getName()).isEqualTo(member1.getName());
        assertThat(members.get(0).getAddress()).isEqualTo(member1.getAddress());
        assertThat(members.get(0).getDescription()).isEqualTo(member1.getDescription());
        assertThat(members.get(1).getName()).isEqualTo(member2.getName());
        assertThat(members.get(1).getAddress()).isEqualTo(member2.getAddress());
        assertThat(members.get(1).getDescription()).isEqualTo(member2.getDescription());

        System.out.println("testGetAll finished");
    }

    @Test
    public void testDeleteAll() {
        memberRepository.save(new Member("Binghe 1", "Binghe 1 address", "Binghe 1 for delete test"));
        memberRepository.save(new Member("Binghe 2", "Binghe 2 address", "Binghe 2 for delete test"));

        memberRepository.deleteAll();
        List<Member> allMembers = memberRepository.getAll();
        assertThat(allMembers).hasSize(0);
    }

    @Test
    public void testGetCount() {
        memberRepository.save(new Member("Binghe 1", "Binghe 1 address", "Binghe 1 for count test"));
        memberRepository.save(new Member("Binghe 2", "Binghe 2 address", "Binghe 2 for count test"));

        int count = memberRepository.getCount();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testUpdate() {
        Member member = new Member("Binghe 1", "Binghe 1 address", "Binghe 1 for update test");
        memberRepository.save(member);

        Member needToUpdateMember = memberRepository.findById(1L).orElse(null);
        needToUpdateMember.updateAddress("updated Binghe 1 address");
        memberRepository.update(needToUpdateMember);

        Optional<Member> updatedMember = memberRepository.findById(1L);
        assertThat(updatedMember).isPresent();
        assertThat(updatedMember.get().getAddress()).isEqualTo("updated Binghe 1 address");
    }
}