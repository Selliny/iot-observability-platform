package ingestion_service.application.port.out;

import ingestion_service.domain.model.SensorReading;

public interface SensorReadingPublisher {
    void publish(SensorReading sensorReading);
}
