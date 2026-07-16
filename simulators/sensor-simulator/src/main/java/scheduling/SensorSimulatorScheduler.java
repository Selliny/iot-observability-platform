package scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import application.usecases.GenerateTemperatureReadingService;
import domain.model.SensorReading;

@Component
public class SensorSimulatorScheduler {
    private static final Logger log = LoggerFactory.getLogger(SensorSimulatorScheduler.class);

    private final GenerateTemperatureReadingService generateTemperatureReadingService;

    public SensorSimulatorScheduler(GenerateTemperatureReadingService generateTemperatureReadingService) {
        this.generateTemperatureReadingService = generateTemperatureReadingService;
    }

    @Scheduled(fixedRateString = "${app.simulator.interval-ms}", initialDelay = 5000)
    public void generateAndPublish() {
        SensorReading reading = generateTemperatureReadingService.generateAndPublish();
        log.debug("Published sensor reading {}", reading);
    }

    public void publishTemperature() {
        try {
            SensorReading reading = generateTemperatureReadingService.generateAndPublish();

            log.info(
                    "Temperature reading published: " +
                            "eventId={}, sensorId={}, value={}, sequence={}",
                    reading.eventId(),
                    reading.sensorId(),
                    reading.value(),
                    reading.sequenceNumber());
        } catch (RuntimeException exception) {
            log.error(
                    "Temperature reading publication failed",
                    exception);
        }
    }
}
