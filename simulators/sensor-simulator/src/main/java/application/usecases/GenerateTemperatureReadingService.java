package application.usecases;

import java.time.Clock;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import application.port.out.SensorReadingPublisher;
import domain.model.SensorReading;
import domain.model.SensorType;
import domain.service.TemperatureGenerator;

public class GenerateTemperatureReadingService {
    private static final String TemperatureUnit = "Celsius";
    private final SensorReadingPublisher sensorReadingPublisher;
    private final TemperatureGenerator temperatureGenerator;
    private final Clock clock;
    private final String tenantId;
    private final String plantId;
    private final String sensorId;
    private final String machineId;
    private final String lineId;
    private final AtomicLong sequence = new AtomicLong(0);

    public GenerateTemperatureReadingService(SensorReadingPublisher sensorReadingPublisher,
            TemperatureGenerator temperatureGenerator,
            Clock clock,
            String tenantId,
            String plantId,
            String lineId,
            String machineId,
            String sensorId) {
        this.sensorReadingPublisher = sensorReadingPublisher;
        this.temperatureGenerator = temperatureGenerator;
        this.clock = clock;
        this.tenantId = tenantId;
        this.plantId = plantId;
        this.lineId = lineId;
        this.machineId = machineId;
        this.sensorId = sensorId;
    }

    public SensorReading generateAndPublish() {
        SensorReading reading = new SensorReading(
                UUID.randomUUID().toString(),
                tenantId,
                plantId,
                lineId,
                machineId,
                sensorId,
                SensorType.TEMPERATURE,
                temperatureGenerator.generate(),
                TemperatureUnit,
                sequence.incrementAndGet(),
                clock.instant());

        sensorReadingPublisher.publish(reading);

        return reading;
    }
}

// 1. gera um eventId;
// 2. gera a temperatura;
// 3. incrementa a sequência;
// 4. registra o horário UTC;
// 5. cria a leitura;
// 6. solicita a publicação.