package com.binghe.domain;

import com.binghe.config.TestDatabaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestDatabaseConfig.class)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
//        memberRepository.deleteAll(); // Clear database before each test
    }

    @Test
    public void testSaveAndFindById() {
        Member member = new Member("binghe", "binghe address", "binghe description");
        memberRepository.save(member);

        // deleteAll로는 1L이면 다른 테스트 실행후 이 테스트 실행시 실패함.
        Member foundMember = memberRepository.findById(1L).orElse(null);
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo(member.getName());
        assertThat(foundMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(foundMember.getDescription()).isEqualTo(member.getDescription());
    }

    @Test
    public void testGetAll() {
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
    }

//
//    @Test
//    public void testDeleteAll() {
//        memberRepository.save(new Member(1L, "John Doe"));
//        memberRepository.save(new Member(2L, "Jane Doe"));
//
//        memberRepository.deleteAll();
//        List<Member> allMembers = memberRepository.getAll();
//        assertEquals(0, allMembers.size());
//    }
//
//    @Test
//    public void testGetCount() {
//        memberRepository.save(new Member(1L, "John Doe"));
//        memberRepository.save(new Member(2L, "Jane Doe"));
//
//        int count = memberRepository.getCount();
//        assertEquals(2, count);
//    }
//
//    @Test
//    public void testUpdate() {
//        Member member = new Member(1L, "John Doe");
//        memberRepository.save(member);
//
//        member.setName("John Smith");
//        memberRepository.update(member);
//
//        Optional<Member> updatedMember = memberRepository.findById(1L);
//        assertTrue(updatedMember.isPresent());
//        assertEquals("John Smith", updatedMember.get().getName());
//    }
}