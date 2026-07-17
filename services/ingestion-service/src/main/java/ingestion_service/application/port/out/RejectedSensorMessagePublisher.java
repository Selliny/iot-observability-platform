package ingestion_service.application.port.out;

import ingestion_service.domain.model.RejectedSensorMessage;

public interface RejectedSensorMessagePublisher {
    void publish(RejectedSensorMessage rejectedSensorMessage);
}
