package processing_service.domain.model;

import java.time.Instant;

public record MachineCurrentState(String tenantId, String machineId, MachineStatus status, Instant stateSince,
        String lastEventId, long lastSequenceNumber, Instant updatedAt) {

}

// tenantId
// → identifica a empresa

// machineId
// → identifica a máquina

// state
// → estado atual

// stateSince
// → instante em que esse estado começou

// lastEventId
// → último evento que atualizou o estado

// lastSequenceNumber
// → maior sequência aceita

// updatedAt
// → instante em que o processing-service atualizou o registro