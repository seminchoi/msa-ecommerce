package com.sem.ecommerce.infra.repository.converter;

import com.sem.ecommerce.domain.order.OrderItemState;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class OrderItemStateConverter extends EnumWriteSupport<OrderItemState> {
}
