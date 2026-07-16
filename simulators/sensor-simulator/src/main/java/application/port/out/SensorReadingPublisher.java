package application.port.out;

import domain.model.SensorReading;

public interface SensorReadingPublisher {
    void publish(SensorReading sensorReading);
}
