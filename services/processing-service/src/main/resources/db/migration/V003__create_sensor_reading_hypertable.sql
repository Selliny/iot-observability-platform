CREATE TABLE processing.sensor_reading (
    event_id             VARCHAR(100) NOT NULL,
    tenant_id            VARCHAR(100) NOT NULL,
    plant_id             VARCHAR(100) NOT NULL,
    line_id              VARCHAR(100) NOT NULL,
    machine_id           VARCHAR(100) NOT NULL,
    sensor_id            VARCHAR(100) NOT NULL,
    sensor_type          VARCHAR(60) NOT NULL,
    value                NUMERIC(20, 6) NOT NULL,
    unit                 VARCHAR(40),
    sequence_number      BIGINT NOT NULL,
    occurred_at          TIMESTAMPTZ NOT NULL,
    ingested_at          TIMESTAMPTZ NOT NULL,
    processed_at         TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (event_id, occurred_at)
);

SELECT create_hypertable(
    'processing.sensor_reading',
    by_range('occurred_at'),
    if_not_exists => TRUE
);

CREATE INDEX idx_sensor_reading_machine_time
    ON processing.sensor_reading (
        tenant_id,
        machine_id,
        occurred_at DESC
    );

CREATE INDEX idx_sensor_reading_sensor_time
    ON processing.sensor_reading (
        tenant_id,
        sensor_id,
        occurred_at DESC
    );