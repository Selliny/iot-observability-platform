CREATE TABLE processing.machine_current_state (
    tenant_id              VARCHAR(100) NOT NULL,
    machine_id             VARCHAR(100) NOT NULL,
    current_state          VARCHAR(30) NOT NULL,
    state_since            TIMESTAMPTZ NOT NULL,
    last_event_id          VARCHAR(100) NOT NULL,
    last_sequence_number   BIGINT NOT NULL,
    updated_at             TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (
        tenant_id,
        machine_id
    )
);

CREATE INDEX idx_machine_current_state_updated_at
    ON processing.machine_current_state (
        updated_at DESC
    );