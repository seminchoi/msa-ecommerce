package com.sem.ecommerce.payment.infra;

import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentStatus;
import com.sem.ecommerce.payment.infra.event.OrderCreatedEvent;
import com.sem.ecommerce.core.mapper.MapperUtils;
import lombok.experimental.UtilityClass;
import org.springframework.messaging.Message;

import java.time.ZonedDateTime;
import java.util.UUID;

@UtilityClass
public class PaymentMapper {
    public Payment fromOrderCreatedMessage(Message<String> message) {
        OrderCreatedEvent event = MapperUtils.fromJson(message.getPayload(), OrderCreatedEvent.class);

        return Payment.builder()
                .id(UUID.randomUUID())
                .orderId(event.orderId())
                .memberId(event.ordererId())
                .amount(event.totalPrice())
                .status(PaymentStatus.PENDING)
                .createdAt(ZonedDateTime.now())
                .build();
    }
}
