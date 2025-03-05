package com.sem.ecommerce.domain;

import java.util.UUID;

public record ReceiverDetails(
        UUID receiverId,
        String address,
        String name,
        String phoneNumber) {
}
