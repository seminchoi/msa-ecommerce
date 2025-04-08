package com.sem.ecommerce.core.event.repository;

import com.sem.ecommerce.core.event.DomainEvent;
import com.sem.ecommerce.core.event.EventPublisher;
import com.sem.ecommerce.core.event.utils.OutBoxEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DomainEventRepository implements EventPublisher {
    private final OutBoxEventRepository outBoxEventRepository;

    public Mono<Void> publish(DomainEvent domainEvent) {
        OutboxEvent outboxEvent = OutBoxEventMapper.from(domainEvent);
        return outBoxEventRepository.save(outboxEvent)
                .then();
    }

    public Mono<Void> publishAll(List<DomainEvent> domainEvent) {
        List<OutboxEvent> outboxEvents = domainEvent.stream().map(OutBoxEventMapper::from).toList();
        return outBoxEventRepository.saveAll(outboxEvents)
                .then();
    }
}
