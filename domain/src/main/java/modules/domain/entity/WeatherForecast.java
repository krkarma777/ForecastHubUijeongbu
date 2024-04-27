package modules.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "weather_forecast")
public class WeatherForecast {
    @Id
    private Long id;

    @Column(name = "base_date", nullable = false)
    private String baseDate;

    @Column(name = "base_time", nullable = false)
    private String baseTime;

    @Column(nullable = false)
    private Integer nx;

    @Column(nullable = false)
    private Integer ny;

    @Column(name = "forecast_date", nullable = false)
    private String fcstDate;

    @Column(name = "forecast_time", nullable = false)
    private String fcstTime;

    @Column(nullable = false)
    private String category;

    @Column(name = "forecast_value", nullable = false)
    private String fcstValue;
}