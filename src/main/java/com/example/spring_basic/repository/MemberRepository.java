package com.example.spring_basic.repository;

import com.example.spring_basic.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /*
    @PersistenceContext // Spring boot jpa에서 알아서 만들어서 주입해줌 (원래는 entityManagerFactory를 만들고 주입을 받아야한다.)
    private EntityManager em;
    */
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId(); // 다른 값들의 노출을 막기위해 될 수 있으면 Id만 리턴
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
