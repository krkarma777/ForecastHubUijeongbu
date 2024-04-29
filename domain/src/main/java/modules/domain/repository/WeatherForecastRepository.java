package modules.domain.repository;

import modules.domain.entity.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    List<WeatherForecast> findByNxAndNy(int nx, int ny);
    boolean existsByBaseDateAndBaseTimeAndNxAndNyAndCategory(String baseDate, String baseTime, int nx, int ny, String category);
}
