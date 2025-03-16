package com.sem.ecommerce.infra.repository.converter;

import com.sem.ecommerce.domain.order.OrderItemState;
import com.sem.ecommerce.domain.refund.RefundState;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class RefundStateConverter extends EnumWriteSupport<RefundState> {
}
