package processing_service.application.port.in;

import processing_service.application.command.ProcessSensorReadingCommand;
import processing_service.application.result.ProcessingResult;

public interface ProcessSensorReadingUseCase {
    ProcessingResult process(ProcessSensorReadingCommand command, ProcessingMetaData metadata);
}
