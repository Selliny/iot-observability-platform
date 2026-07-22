package processing_service.application.port.out;

import processing_service.application.result.MachineStateUpdateResult;
import processing_service.domain.model.MachineCurrentState;

public interface MachineCurrentStateRepository {
    MachineStateUpdateResult saveIfNewer(MachineCurrentState state);
}
