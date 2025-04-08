package com.sem.ecommerce.core.event.outbox;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface OutBoxEventRepository extends R2dbcRepository<OutboxEvent, UUID> {
}
