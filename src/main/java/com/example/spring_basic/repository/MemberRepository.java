package com.example.spring_basic.repository;

import com.example.spring_basic.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext // Spring boot jpa에서 알아서 주입해줌
    private EntityManager entityManager;

    public Long save(Member member){
        entityManager.persist(member);
        return member.getId(); // 다른 값들의 노출을 막기위해 될 수 있으면 Id만 리턴
    }

    public Member find(Long id){
        return entityManager.find(Member.class, id);
    }
}
