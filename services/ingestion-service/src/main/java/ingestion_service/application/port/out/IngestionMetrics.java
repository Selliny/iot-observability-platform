package ingestion_service.application.port.out;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class IngestionMetrics {
    private final MeterRegistry meterRegistry;

    public IngestionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void registerAccepted() {
        meterRegistry.counter("ingestion.sensor_readings.accepted").increment();
    }

    public void registerRejected(String reasonCode) {
        meterRegistry.counter("ingestion.sensor_readings.rejected", "reason", reasonCode).increment();
    }
}
