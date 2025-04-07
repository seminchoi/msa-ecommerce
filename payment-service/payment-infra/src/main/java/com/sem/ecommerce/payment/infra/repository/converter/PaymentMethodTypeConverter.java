package com.sem.ecommerce.payment.infra.repository.converter;

import com.sem.ecommerce.payment.domain.PaymentMethodType;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class PaymentMethodTypeConverter extends EnumWriteSupport<PaymentMethodType> {
}