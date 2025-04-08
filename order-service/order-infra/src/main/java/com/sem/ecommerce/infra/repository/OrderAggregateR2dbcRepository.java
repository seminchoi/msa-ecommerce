package com.sem.ecommerce.infra.repository;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.infra.repository.model.OrderItemModel;
import com.sem.ecommerce.infra.repository.model.OrderModel;
import com.sem.ecommerce.infra.repository.model.OrderModelComposite;
import com.sem.ecommerce.core.event.repository.DomainEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderAggregateR2dbcRepository implements OrderAggregateRepository {
    private final OrderR2dbcRepository orderRepository;
    private final OrderItemR2dbcRepository orderItemRepository;
    private final DomainEventRepository outBoxEventRepository;

    @Override
    public Mono<Void> save(Order order) {
        OrderModelComposite orderModelComposite = OrderModelComposite.from(order, true);

        return orderRepository.save(orderModelComposite.order())
                .thenMany(orderItemRepository.saveAll(orderModelComposite.orderItems()))
                .then(outBoxEventRepository.publishAll(order.getEvents()))
                .doOnSuccess(unused -> order.clearEvents())
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
