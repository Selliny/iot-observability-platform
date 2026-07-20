package processing_service.configuration;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import processing_service.application.port.in.ProcessSensorReadingUseCase;
import processing_service.application.port.out.ProcessedEventRepository;
import processing_service.application.port.out.SensorReadingRepository;
import processing_service.application.usecase.ProcessSensorReadingService;

@Configuration
public class UseCaseConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ProcessSensorReadingUseCase processSensorReadingUseCase(
            ProcessedEventRepository processedEventRepository,
            SensorReadingRepository sensorReadingRepository,
            Clock clock) {
        return new ProcessSensorReadingService(
                processedEventRepository,
                sensorReadingRepository,
                clock);
    }
}
