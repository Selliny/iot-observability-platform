package ingestion_service.adapters.in;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ingestion_service.application.port.in.IngestSensorReadingUseCase;
import ingestion_service.application.result.IngestionResult;

import org.springframework.http.ResponseEntity;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/sensor-readings")
public class SensorReadingController {
    private final IngestSensorReadingUseCase ingestSensorReadingUseCase;

    public SensorReadingController(IngestSensorReadingUseCase useCase) {
        this.ingestSensorReadingUseCase = useCase;
    }

    @PostMapping
    public ResponseEntity<SensorReadingResponse> ingest(@Valid @RequestBody SensorReadingRequest request) {
        IngestionResult result = ingestSensorReadingUseCase.ingest(request.toCommand());
        SensorReadingResponse response = SensorReadingResponse.from(result);
        URI location = URI.create("/api/v1/sensor-readings/" + result.eventId());
        return ResponseEntity.accepted().location(location).body(response);
    }
}
