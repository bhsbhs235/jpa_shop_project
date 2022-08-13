package com.example.spring_basic.service;

import com.example.spring_basic.domain.Address;
import com.example.spring_basic.domain.Member;
import com.example.spring_basic.domain.Order;
import com.example.spring_basic.domain.OrderStatus;
import com.example.spring_basic.domain.item.Book;
import com.example.spring_basic.domain.item.Item;
import com.example.spring_basic.exception.NotEnoughStockException;
import com.example.spring_basic.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    /*
        가장 좋은 테스트는
        1. 단위(메서드와 유사)로 테스트 한다
        2. DB 같은거 안붙고 Mocking 해서 쓰는것이 빠르고 바람직하다.
     */

    @Test
    @DisplayName("상품주문")
    public void orderItem() {
        // given
        Member member = createMember();

        Book book = createBook("JPA 기본", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, order.getStatus(), " 상품 주문시 상태는 ORDER로 정상");
        assertEquals(1, order.getOrderItems().size(), "주문한 상품 종류수 정상");
        assertEquals(10000 * orderCount, order.getTotalPrice(), "주문 가격 정상");
        assertEquals(8, book.getStockQuantity(), "재고수 정상");
    }

    @Test
    @DisplayName("상품주문 재고수량 초과")
    public void stockOver() {
        // given
        Member member = createMember();
        Item item = createBook("스프링", 10000, 10);

        int orderCount = 11 ;

        // when
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
            // 정확한 테스트는 order 보다 Item의 removeStock을 "단위" 테스트로 하는게 좋다.
        });

        //then
    }

    @Test
    @DisplayName("주문취소")
    public void orderCancel() {
        // given
        Member member = createMember();
        Book item = createBook("스프링", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // 주문한거 까지가 given이다

        // when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus(), "주문 취소시 상태는 CANCEL이다.");
        assertEquals(10, item.getStockQuantity(), "주문이 취소되면 재고가 증가해야한다.");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "서초구", "19-20"));
        em.persist(member);
        return member;
    }
}