package com.sem.ecommerce.service.dto.request;

import com.sem.ecommerce.infra.dto.OrderItemDto;
import com.sem.ecommerce.infra.dto.ReceiverDto;

import java.util.List;

public record CreateOrderRequest (
        List<OrderItemDto> orderItemDtos,
        ReceiverDto receiver
) {
}
