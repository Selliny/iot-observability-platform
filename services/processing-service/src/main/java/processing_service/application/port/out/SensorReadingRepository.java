package processing_service.application.port.out;

import processing_service.domain.model.SensorReading;

public interface SensorReadingRepository {
    void save(SensorReading reading);
}
