package com.sem.ecormmerce.core.event.repository;

import com.sem.ecormmerce.core.event.DomainEvent;
import com.sem.ecormmerce.core.event.utils.OutBoxEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DomainEventRepository {
    private final OutBoxEventRepository outBoxEventRepository;

    public Mono<Void> save(DomainEvent domainEvent) {
        OutboxEvent outboxEvent = OutBoxEventMapper.from(domainEvent);
        return outBoxEventRepository.save(outboxEvent)
                .then();
    }

    public Mono<Void> saveAll(List<DomainEvent> domainEvent) {
        List<OutboxEvent> outboxEvents = domainEvent.stream().map(OutBoxEventMapper::from).toList();
        return outBoxEventRepository.saveAll(outboxEvents)
                .then();
    }
}
