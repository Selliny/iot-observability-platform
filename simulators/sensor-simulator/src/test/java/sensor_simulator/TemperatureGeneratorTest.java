package sensor_simulator;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import domain.service.TemperatureGenerator;

class TemperatureGeneratorTest {

    private final TemperatureGenerator generator = new TemperatureGenerator();

    @Test
    void generateTemperatureInsideConfiguredRange() {
        for (int index = 0; index < 100; index++) {
            BigDecimal temperature = generator.generate();
            assertThat(temperature).isBetween(new BigDecimal("45.00"), new BigDecimal("85.00"));
        }
    }
}
