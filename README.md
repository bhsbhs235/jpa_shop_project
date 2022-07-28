#Spring project

- jpa 

주의

- @Setter는 편의상 해둔거지만 실무에서는 많은 문제를 야기할 수 있으므로 쓰지 않는 것이 좋다
- 실무에서는 @ManyToMany는 쓰지 말자
- @Id @Column(name = "member_id") 테이블에는 id 말고 컬럼을 정해주는 것이 좋다
- 값 타입(Embeddable)은 변경 불가능하게 설계해야 한다.
    JPA 스펙상 엔티티나 임베디드 타입은 자바 기본 생성자를 public 또는 protected로 설정해야 한다. 
  protected가 안전하다 JPA가 이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을
    사용할 수 있도록 지원해야 하기 때문이다.
