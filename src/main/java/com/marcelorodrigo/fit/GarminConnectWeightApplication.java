package com.marcelorodrigo.fit;

import com.marcelorodrigo.fit.properties.KeepAliveProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(KeepAliveProperties.class)
@SpringBootApplication
public class GarminConnectWeightApplication {

    public static void main(String[] args) {
        SpringApplication.run(GarminConnectWeightApplication.class, args);
    }

}
