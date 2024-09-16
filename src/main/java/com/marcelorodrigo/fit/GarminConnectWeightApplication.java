package com.marcelorodrigo.fit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GarminConnectWeightApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarminConnectWeightApplication.class, args);
	}

}
