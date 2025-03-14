package com.sem.ecommerce.infra.repository.converter;

import com.sem.ecommerce.domain.order.ShippingState;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class ShippingStateConverter extends EnumWriteSupport<ShippingState> {
}
