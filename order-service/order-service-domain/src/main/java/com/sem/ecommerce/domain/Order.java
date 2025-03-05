package com.sem.ecommerce.domain;

import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private ReceiverDetails receiverDetails;
    private OrderState orderState;
    private List<OrderProduct> orderProduct;
}
