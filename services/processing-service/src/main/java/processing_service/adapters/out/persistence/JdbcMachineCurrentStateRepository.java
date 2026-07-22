package processing_service.adapters.out.persistence;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import processing_service.application.port.out.MachineCurrentStateRepository;
import processing_service.application.result.MachineStateUpdateResult;
import processing_service.domain.model.MachineCurrentState;

@Repository
public class JdbcMachineCurrentStateRepository implements MachineCurrentStateRepository {
    private final JdbcClient jdbcClient;

    public JdbcMachineCurrentStateRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public MachineStateUpdateResult saveIfNewer(
            MachineCurrentState state) {
        int affectedRows = jdbcClient
                .sql("""
                        INSERT INTO processing.machine_current_state (tenant_id,machine_id,current_state,state_since,last_event_id,last_sequence_number,updated_at)
                        VALUES (:tenantId,:machineId,:currentState,:stateSince,:lastEventId,:lastSequenceNumber,:updatedAt)
                        ON CONFLICT (tenant_id,machine_id)
                        DO UPDATE SET
                            current_state = EXCLUDED.current_state,
                            state_since = EXCLUDED.state_since,
                            last_event_id = EXCLUDED.last_event_id,
                            last_sequence_number =
                                EXCLUDED.last_sequence_number,
                            updated_at = EXCLUDED.updated_at
                        WHERE
                            EXCLUDED.last_sequence_number >
                            processing.machine_current_state
                                .last_sequence_number
                        """)
                .param("tenantId", state.tenantId())
                .param("machineId", state.machineId())
                .param("currentState", state.status().name())
                .param("stateSince", state.stateSince())
                .param("lastEventId", state.lastEventId())
                .param(
                        "lastSequenceNumber",
                        state.lastSequenceNumber())
                .param("updatedAt", state.updatedAt())
                .update();

        if (affectedRows == 1) {
            return determineInsertedOrUpdated(state);
        }

        return MachineStateUpdateResult.OUTDATED;
    }

    private MachineStateUpdateResult determineInsertedOrUpdated(
            MachineCurrentState state) {
        Long count = jdbcClient.sql("""
                SELECT COUNT(*)
                FROM processing.machine_current_state
                WHERE tenant_id = :tenantId
                  AND machine_id = :machineId
                  AND last_event_id = :lastEventId
                """)
                .param("tenantId", state.tenantId())
                .param("machineId", state.machineId())
                .param("lastEventId", state.lastEventId())
                .query(Long.class)
                .single();

        return count != null && count == 1
                ? MachineStateUpdateResult.UPDATED
                : MachineStateUpdateResult.INSERTED;
    }
}
