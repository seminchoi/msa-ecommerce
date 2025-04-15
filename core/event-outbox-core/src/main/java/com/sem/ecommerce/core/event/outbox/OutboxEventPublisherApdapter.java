package com.sem.ecommerce.core.event.outbox;

import com.sem.ecommerce.core.event.DomainEvent;
import com.sem.ecommerce.core.event.EventPublisher;
import com.sem.ecommerce.core.event.utils.OutBoxEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxEventPublisherApdapter implements EventPublisher {
    private final OutBoxEventRepository outBoxEventRepository;

    public Mono<Void> publish(DomainEvent domainEvent) {
        OutboxEvent outboxEvent = OutBoxEventMapper.from(domainEvent);
        return outBoxEventRepository.save(outboxEvent)
                .then();
    }

    public Mono<Void> publishAll(List<DomainEvent> domainEvents) {
        List<OutboxEvent> outboxEvents = domainEvents.stream()
                .map(OutBoxEventMapper::from)
                .toList();
        return outBoxEventRepository.saveAll(outboxEvents)
                .then();
    }
}