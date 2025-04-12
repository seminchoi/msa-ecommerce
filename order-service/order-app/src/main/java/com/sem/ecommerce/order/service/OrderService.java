package com.sem.ecommerce.order.service;

import com.sem.ecommerce.core.event.EventPublisher;
import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.domain.order.OrderItem;
import com.sem.ecommerce.domain.order.OrderItems;
import com.sem.ecommerce.domain.order.OrderState;
import com.sem.ecommerce.domain.order.Receiver;
import com.sem.ecommerce.domain.order.port.OrderRepositoryPort;
import com.sem.ecommerce.domain.order.port.OrderServicePort;
import com.sem.ecommerce.order.service.command.CreateOrderCommand;
import com.sem.ecommerce.order.service.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServicePort {
    private final OrderRepositoryPort orderRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Mono<UUID> createOrder(CreateOrderCommand command) {
        Order order = createOrderFromCommand(command);
        return orderRepository.save(order)
                .then(eventPublisher.publishAll(order.getEvents()))
                .then(Mono.fromRunnable(order::clearEvents))
                .then(Mono.fromCallable(order::getId));
    }

    /**
     * CreateOrderCommand를 Order 엔티티로 변환합니다.
     * @param command 주문 생성 명령
     * @return 생성된 Order 엔티티
     */
    private Order createOrderFromCommand(CreateOrderCommand command) {
        Receiver receiver = command.receiver().toDomain();

        List<OrderItem> orderItems = command.orderItems().stream()
                .map(OrderItemDto::toDomain)
                .collect(Collectors.toList());

        OrderItems items = new OrderItems(orderItems);

        return Order.create(command.ordererId(), receiver, items);
    }

    @Override
    @Transactional
    public Mono<Void> handleExpiredOrderCreated(UUID orderId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    if (order.getOrderState() == OrderState.PENDING) {
                        order.markAsOrderFailed();
                        return orderRepository.save(order);
                    } else {
                        return Mono.empty();
                    }
                });
    }

    public void cancelOrder() {
        //주문을 취소한다.
        //주문 취소 이벤트를 제품 서비스로 발행한다. (재고 복구)
        //주문 취소 이벤트를 결제 서비스로 발행한다. (결제 취소)
    }
}
