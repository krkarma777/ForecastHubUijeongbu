package modules.domain.repository;

import modules.domain.entity.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 날씨 예보 레포지토리 인터페이스
@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    // x, y 좌표로 날씨 예보 조회
    List<WeatherForecast> findByNxAndNy(int nx, int ny);

    // 주어진 기준 날짜, 시간, x, y 좌표 및 카테고리로 날씨 예보가 존재하는지 확인
    boolean existsByBaseDateAndBaseTimeAndNxAndNyAndCategory(String baseDate, String baseTime, int nx, int ny, String category);
}
