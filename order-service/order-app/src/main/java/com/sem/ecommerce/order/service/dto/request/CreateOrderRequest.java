package com.sem.ecommerce.order.service.dto.request;


import com.sem.ecommerce.order.service.dto.OrderItemDto;
import com.sem.ecommerce.order.service.dto.ReceiverDto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest (
        UUID ordererId,
        List<OrderItemDto> orderItemDtos,
        ReceiverDto receiver
) {
}
