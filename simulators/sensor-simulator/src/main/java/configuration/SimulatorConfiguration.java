package configuration;

import java.time.Clock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import application.port.out.SensorReadingPublisher;
import application.usecases.GenerateTemperatureReadingService;
import domain.service.TemperatureGenerator;

@Configuration
public class SimulatorConfiguration {
    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public TemperatureGenerator temperatureGenerator() {
        return new TemperatureGenerator();
    }

    @Bean
    public GenerateTemperatureReadingService generateTemperatureReadingService(
            SensorReadingPublisher sensorReadingPublisher,
            TemperatureGenerator temperatureGenerator,
            Clock clock,
            @Value("${app.simulator.tenant-id}") String tenantId,
            @Value("${app.simulator.plant-id}") String plantId,
            @Value("${app.simulator.line-id}") String lineId,
            @Value("${app.simulator.machine-id}") String machineId,
            @Value("${app.simulator.sensor-id}") String sensorId) {
        return new GenerateTemperatureReadingService(sensorReadingPublisher, temperatureGenerator, clock, tenantId,
                plantId, lineId, machineId, sensorId);
    }
}
