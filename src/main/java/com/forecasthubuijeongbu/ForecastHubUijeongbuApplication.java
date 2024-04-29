package com.forecasthubuijeongbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"modules.domain.entity"})
@ComponentScan(basePackages = {"com.forecasthubuijeongbu", "modules"})
public class ForecastHubUijeongbuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForecastHubUijeongbuApplication.class, args);
    }

}
