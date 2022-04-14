package com.binghe.springbootjpaexample1;

import com.binghe.springbootjpaexample1.db_hello_world.MemberTest;
import com.binghe.springbootjpaexample1.db_hello_world.MemberTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberTestRepositoryTest {

    @Autowired
    private MemberTestRepository memberRepository;

    @Test
//    @Rollback(false)
    void saveAndFindMember() {
        // given
        MemberTest memberTest = new MemberTest();
        memberTest.setUsername("binghe");

        // when
        Long savedMemberId = memberRepository.save(memberTest);
        MemberTest savedMemberTest = memberRepository.find(savedMemberId);

        // then
        assertThat(savedMemberTest.getId()).isEqualTo(savedMemberId);
        assertThat(savedMemberTest.getUsername()).isEqualTo(memberTest.getUsername());
        assertThat(savedMemberTest).isEqualTo(memberTest);
        assertThat(savedMemberTest).isSameAs(memberTest); // 같은 트랜잭션이므로 같은 영속성 컨텍스트에서 가져오기때문에 둘이 같은 객체이다.
    }
}
