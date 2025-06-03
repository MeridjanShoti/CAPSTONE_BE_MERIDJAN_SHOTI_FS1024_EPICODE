package it.epicode.simposiodermedallo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimposioDerMedalloApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimposioDerMedalloApplication.class, args);
	}

}
