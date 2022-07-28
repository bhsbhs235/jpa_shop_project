package com.example.spring_basic.repository;

import com.example.spring_basic.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    // @Rollback(value = false) // Test가 끝나면 Rollback을 해줌 따라서 false
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setName("A");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember).isEqualTo(member); // 같은 영속성 컨텍스트 안에서는 동일하 select 문도 날리지 않는다

    }



}