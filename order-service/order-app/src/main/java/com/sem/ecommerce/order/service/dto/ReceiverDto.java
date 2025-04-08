package com.sem.ecommerce.order.service.dto;

import com.sem.ecommerce.domain.order.Receiver;

public record ReceiverDto(
        String name,
        String phoneNumber,
        String address
) {
    public Receiver toDomain() {
        return Receiver.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
