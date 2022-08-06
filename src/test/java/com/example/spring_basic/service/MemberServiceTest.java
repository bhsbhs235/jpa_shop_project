package com.example.spring_basic.service;

import com.example.spring_basic.domain.Member;
import com.example.spring_basic.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // junit5 는 @RunWith(SpringRunner.class)가 필요없음
@Transactional // 테스트 끝나면 롤백함
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    // @Rollback(value = false) // em.persist 까지는 DB에 반영되지 않는다 커밋을 해야 적용되는데 위에 @Transactional로 롤백을 하기 때문에 db에 insert 하지 않는다
    @DisplayName("회원가입")
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("bae");

        // when
        Long saveId = memberService.join(member);

        //then
        em.flush(); // 영속성 컨텍스트에 있는 것이 적용된다. (롤백 false 안해줘도 쿼리를 볼 수 있다)
        assertEquals(member, memberRepository.findOne(saveId));
        //JPA에서 같은 트랜잭션 안에서 PK값이 같으면 같은 영속성 컨텍스트에서 관리되며 같은 엔티티다.
    }

    @Test
    @DisplayName("중복 회원 에러")
    public void 중복_회원_에러() {
        // given
        Member member = new Member();
        member.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        //then
    }
}