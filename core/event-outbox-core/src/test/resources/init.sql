CREATE TABLE IF NOT EXISTS outbox_event (
    id UUID PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    event_key VARCHAR(255) NOT NULL,
    output_binding VARCHAR(255) NOT NULL,
    expiration_minutes INT NOT NULL,
    payload TEXT NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP,
    status VARCHAR(20) NOT NULL
    );

-- 필요한 인덱스 추가
CREATE INDEX IF NOT EXISTS idx_outbox_event_status ON outbox_event(status);
CREATE INDEX IF NOT EXISTS idx_outbox_event_expires_at ON outbox_event(expires_at);