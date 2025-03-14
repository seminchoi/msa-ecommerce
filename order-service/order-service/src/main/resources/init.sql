CREATE TYPE order_state AS ENUM ('NONE', 'PAID');
CREATE TYPE order_item_state AS ENUM ('NONE', 'REFUND_REQUESTED');
CREATE TYPE shipping_state AS ENUM ('READY', 'STARTED', 'COMPLETED');

CREATE TABLE orders
(
    id                    UUID PRIMARY KEY,
    orderer_id            UUID        NOT NULL,
    order_state           order_state NOT NULL,
    receiver_address      VARCHAR(200),
    receiver_name         VARCHAR(32),
    receiver_phone_number VARCHAR(32)
);

CREATE TABLE order_item
(
    id               UUID PRIMARY KEY,
    order_id         UUID             NOT NULL REFERENCES orders (id),
    catalog_id       UUID             NOT NULL,
    unit_price       INT8             NOT NULL,
    quantity         INT4             NOT NULL,
    order_item_state order_item_state NOT NULL,
    shipping_state   shipping_state   NOT NULL
);


-----------------------------------------------------------------------------------------

CREATE TYPE refund_state AS ENUM ('REQUESTED', 'PROCESSING', 'COMPLETED', 'REJECTED');

CREATE TABLE refund
(
    id                      UUID PRIMARY KEY,
    order_item_id           UUID         NOT NULL REFERENCES order_item (id),
    refund_state            refund_state NOT NULL,
    refund_request_amount   INT4         NOT NULL,
    refund_completed_amount INT4         NOT NULL,
    reason                  VARCHAR(200)
);


