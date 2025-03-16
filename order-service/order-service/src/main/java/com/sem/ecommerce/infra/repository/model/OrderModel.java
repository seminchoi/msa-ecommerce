package com.sem.ecommerce.infra.repository.model;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.domain.order.OrderState;
import com.sem.ecommerce.domain.order.Receiver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel implements Persistable<UUID> {
    @Id
    @Column("id")
    private UUID id;

    @Column("orderer_id")
    private UUID ordererId;

    @Column("order_state")
    private OrderState orderState;

    @Column("receiver_address")
    private String receiverAddress;

    @Column("receiver_name")
    private String receiverName;

    @Column("receiver_phone_number")
    private String receiverPhoneNumber;

    @Transient
    private boolean isNew;

    public static OrderModel from(Order order) {
        return from(order, false);
    }

    public static OrderModel from(Order order, boolean isNew) {
        Receiver receiver = order.getReceiver();
        return OrderModel.builder()
                .id(order.getId())
                .ordererId(order.getOrdererId())
                .orderState(order.getOrderState())
                .receiverAddress(receiver.address())
                .receiverName(receiver.name())
                .receiverPhoneNumber(receiver.phoneNumber())
                .isNew(isNew)
                .build();
    }
}