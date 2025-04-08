package com.sem.ecommerce.order.service;

//import com.sem.ecommerce.domain.order.Order;
//import com.sem.ecommerce.domain.order.port.OrderServicePort;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService implements OrderServicePort {
//    @Override
//    public Mono<UUID> createOrder(Order order) {
//
//        return Mono.empty();
//    }
//
//    public void cancelOrder() {
//        //주문을 취소한다.
//        //주문 취소 이벤트를 제품 서비스로 발행한다. (재고 복구)
//        //주문 취소 이벤트를 결제 서비스로 발행한다. (결제 취소)
//    }
//}
