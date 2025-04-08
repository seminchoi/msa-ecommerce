package com.sem.ecommerce.order.service.command;

import com.sem.ecommerce.infra.dto.OrderItemDto;
import com.sem.ecommerce.infra.dto.ReceiverDto;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        UUID ordererId,
        ReceiverDto receiver,
        List<OrderItemDto> orderItems
) {

}
