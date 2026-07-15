package ingestion_service.configuration;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ingestion_service.application.port.in.IngestSensorReadingUseCase;
import ingestion_service.application.port.out.SensorReadingPublisher;
import ingestion_service.application.usecase.IngestSensorReadingService;

@Configuration
public class UseCaseConfiguration {
    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public IngestSensorReadingUseCase ingestSensorReadingUseCase(SensorReadingPublisher publisher, Clock clock) {
        return new IngestSensorReadingService(publisher, clock);
    }
}
