package domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class TemperatureGenerator {
    private static final double MinimumTemperature = 45.0;
    private static final double MaximumTemperature = 85.0;

    public BigDecimal generate() {
        double temperature = ThreadLocalRandom.current().nextDouble(MinimumTemperature, MaximumTemperature);
        return BigDecimal.valueOf(temperature).setScale(2, RoundingMode.HALF_UP);
    }
}
