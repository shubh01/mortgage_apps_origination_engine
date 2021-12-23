package com.barclays.mortgage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MortgageAppsOriginationEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MortgageAppsOriginationEngineApplication.class, args);
	}

}
