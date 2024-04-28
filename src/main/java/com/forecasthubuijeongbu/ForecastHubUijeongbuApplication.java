package com.forecasthubuijeongbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"modules.domain.entity"})
public class ForecastHubUijeongbuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForecastHubUijeongbuApplication.class, args);
    }

}
