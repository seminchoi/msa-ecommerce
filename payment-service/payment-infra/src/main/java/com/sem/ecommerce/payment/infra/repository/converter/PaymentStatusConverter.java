package com.sem.ecommerce.payment.infra.repository.converter;

import com.sem.ecommerce.payment.domain.PaymentStatus;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class PaymentStatusConverter extends EnumWriteSupport<PaymentStatus> {
}