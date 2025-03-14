package com.sem.ecommerce.infra.repository.converter;

import com.sem.ecommerce.domain.order.OrderState;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class OrderStateConverter extends EnumWriteSupport<OrderState> {
}
