package com.sem.ecommerce.payment.infra.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface ProcessedEventRepository extends R2dbcRepository<ProcessedEvent, UUID> {
}
