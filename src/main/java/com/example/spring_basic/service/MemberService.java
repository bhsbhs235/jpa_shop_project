package com.example.spring_basic.service;

import com.example.spring_basic.domain.Member;
import com.example.spring_basic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기 전용으로 성능을 최적화 해줌
@RequiredArgsConstructor // final로 된 필드만 생성자로 만들어준다
public class MemberService {

    private final MemberRepository memberRepository; // final로 잡아주면 컴파일 시점에서 memberRepository가 초기화가 되지 않는 것을 잡아준다.

    /*@Autowired // 생성자로 주입해주면 test 클래스에서 Mockito 객체를 주입해줄수가 있다.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    /**
     *  회원 가입
     */
    @Transactional // 기본값은 false로 읽기 전용 false로 덮어준다
    public Long join(Member member){
        validateDuplicateMember(member);
        Long saveId = memberRepository.save(member);
        return saveId;
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
