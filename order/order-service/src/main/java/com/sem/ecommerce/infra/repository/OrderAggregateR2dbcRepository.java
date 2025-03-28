package com.sem.ecommerce.infra.repository;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.infra.repository.model.OrderItemModel;
import com.sem.ecommerce.infra.repository.model.OrderModel;
import com.sem.ecommerce.infra.repository.model.OrderModelComposite;
import com.sem.ecormmerce.core.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderAggregateR2dbcRepository implements OrderAggregateRepository {
    private final OrderR2dbcRepository orderRepository;
    private final OrderItemR2dbcRepository orderItemRepository;

    @Override
    public Mono<Void> save(Order order) {
        OrderModelComposite orderModelComposite = OrderModelComposite.from(order, true);

        List<DomainEvent> events = order.getEvents();

        return orderRepository.save(orderModelComposite.order())
                .thenMany(orderItemRepository.saveAll(orderModelComposite.orderItems()))
                .then();
    }

    @Override
    public Mono<Order> findById(UUID orderId) {
        Mono<OrderModel> orderModel = orderRepository.findById(orderId);
        Flux<OrderItemModel> orderItemModels = orderItemRepository.findAllByOrderId(orderId);
        return orderModel.zipWith(orderItemModels.collectList())
                .map(tuple -> OrderModelComposite.builder()
                        .order(tuple.getT1())
                        .orderItems(tuple.getT2())
                        .build()
                        .toDomain()
                );
    }
}
