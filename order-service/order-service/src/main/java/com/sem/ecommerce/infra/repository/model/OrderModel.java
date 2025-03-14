package com.sem.ecommerce.infra.repository.model;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.domain.order.OrderItem;
import com.sem.ecommerce.domain.order.OrderState;
import com.sem.ecommerce.domain.order.Receiver;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("orders")
@Builder
public record OrderModel(
        @Id
        @Column("id")
        UUID id,

        @Column("orderer_id")
        UUID ordererId,

        @Column("order_state")
        OrderState orderState,

        @Column("receiver_address")
        String receiverAddress,

        @Column("receiver_name")
        String receiverName,

        @Column("receiver_phone_number")
        String receiverPhoneNumber
) {
}