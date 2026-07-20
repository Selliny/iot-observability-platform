CREATE TABLE processing.processed_event (
    event_id             VARCHAR(100) PRIMARY KEY,
    topic_name           VARCHAR(200) NOT NULL,
    partition_number     INTEGER NOT NULL,
    kafka_offset         BIGINT NOT NULL,
    processed_at         TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_processed_event_processed_at
    ON processing.processed_event (processed_at);