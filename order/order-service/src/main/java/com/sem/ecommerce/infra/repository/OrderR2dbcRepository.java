package com.sem.ecommerce.infra.repository;

import com.sem.ecommerce.infra.repository.model.OrderModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface OrderR2dbcRepository extends R2dbcRepository<OrderModel, UUID> {
}
