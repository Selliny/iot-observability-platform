package processing_service.application.result;

public enum MachineStateUpdateResult {
    INSERTED, UPDATED, OUTDATED, NOT_APPLICABLE
}

// INSERTED
// → máquina ainda não tinha estado

// UPDATED
// → evento mais recente atualizou a máquina

// OUTDATED
// → evento antigo foi ignorado

// NOT_APPLICABLE
// → leitura não é do tipo MACHINE_STATE