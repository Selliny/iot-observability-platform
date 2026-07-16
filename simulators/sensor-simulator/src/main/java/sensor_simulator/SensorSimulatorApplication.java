package sensor_simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = { "sensor_simulator", "adapters", "application", "configuration", "domain",
        "scheduling" })
public class SensorSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorSimulatorApplication.class, args);
	}

}
