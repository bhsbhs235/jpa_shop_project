package com.example.spring_basic.service;

import com.example.spring_basic.domain.Delivery;
import com.example.spring_basic.domain.Member;
import com.example.spring_basic.domain.Order;
import com.example.spring_basic.domain.OrderItem;
import com.example.spring_basic.domain.item.Item;
import com.example.spring_basic.repository.ItemRepository;
import com.example.spring_basic.repository.MemberRepository;
import com.example.spring_basic.repository.OrderRepository;
import com.example.spring_basic.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        /*
         원래는 delivery 랑 OrderItem을 repository에서 persist해주고 Order를 저장해야 하는데, CascadeType.ALL 옵션 덕분에 같이 적용된다.

         주의
         참조하는 곳이 하나일 때 사용해야함, 특정 엔티티가 개인 소유할 때 사용 cascade로 지웠는데 다른데는 반영안됨 뒤죽박죽됨
         ** OrderItem, Delivery는 Order에서만 참조한다. **
         */

        /*
            참고
            - 주문 서비스의 주문과 주문 취소 메서드를 보면 비즈니스 로직 대부분이 엔티티에 있다. 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
            이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라한다.
            반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 트랜잭션 스크립트 패턴이라 한다.
            createOrderItem 메서드에 있는 내용들이 OrderService에 있는 것을 트랜잭션 스크립트 패턴이라 한다.
            상황,문맥에 따라 다르기 때문에 적당한 패턴을 사용하면 된다.
         */

        return order.getId();
    }


    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();

        /*
        JPA 쓰는 이유
         - Mybatis 처럼 직접 sql문을 다뤄야 하는 라이브러리는 Order 상태값과 OrderItem 재고를 변경하는 Update문을 직접 작성하여 써야함
         - JPA는 데이터만 바꿔주면 변경 감지(더티 체킹)을 해서 알아서 업데이트 쿼리문을 날려준다
         */
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
