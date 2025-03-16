package com.sem.ecommerce.service.dto.request;

import com.sem.ecommerce.service.dto.share.OrderItem;
import com.sem.ecommerce.service.dto.share.ReceiverDto;

import java.util.List;

public record CreateOrderRequest (
        List<OrderItem> orderItems,
        ReceiverDto receiver
) {
}
