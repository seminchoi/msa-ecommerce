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

CREATE TABLE IF NOT EXISTS outbox_event (
                                            id UUID PRIMARY KEY,
                                            aggregate_id VARCHAR(255) NOT NULL,
    event_key VARCHAR(255) NOT NULL,
    output_binding VARCHAR(255) NOT NULL,
    expiration_minutes INT NOT NULL,
    payload TEXT NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE,
                             status VARCHAR(20) NOT NULL
    );

-- 인덱스 추가 (성능 최적화)
CREATE INDEX IF NOT EXISTS idx_outbox_event_status ON outbox_event(status);
CREATE INDEX IF NOT EXISTS idx_outbox_event_expires_at ON outbox_event(expires_at);
