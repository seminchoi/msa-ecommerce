package com.sem.ecommerce.service;

import com.sem.ecommerce.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    public void createOrder(Order order) {
        //주문을 생성한다.
        //주문 정보를 저장한다.
        //주문 생성 이벤트를 제품 서비스로 발행한다. (재고 감소)
        //주문 생성 이벤트를 결제 서비스로 발행한다. (결제 처리)
    }

    public void cancelOrder() {
        //주문을 취소한다.
        //주문 취소 이벤트를 제품 서비스로 발행한다. (재고 복구)
        //주문 취소 이벤트를 결제 서비스로 발행한다. (결제 취소)
    }
}
