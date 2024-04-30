package modules.application.inquireapplication.controller;

import modules.application.syncapplication.service.WeatherService;
import modules.domain.dto.WeatherForecastRequestDTO;
import modules.domain.entity.WeatherForecast;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 날씨 컨트롤러
@RestController
@RequestMapping("/api/forecasts")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // 날씨 예보 조회 및 저장
    @PostMapping
    public ResponseEntity<?> fetchAndSaveForecast(@RequestBody @Validated WeatherForecastRequestDTO requestDTO,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(collectErrors(bindingResult));
        }
        weatherService.fetchWeatherData(requestDTO).forEach(weatherService::saveForecastData);
        return ResponseEntity.ok().build();
    }

    // 날씨 예보 조회
    @GetMapping
    public ResponseEntity<?> getForecasts(@RequestParam(value = "nx", required = false) Integer nx,
                                          @RequestParam(value = "ny", required = false) Integer ny) {
        if (nx == null || ny == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "좌표를 정확히 입력해주십시오."));
        }

        List<WeatherForecast> forecasts = weatherService.getForecasts(nx, ny);
        if (forecasts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(forecasts);
    }

    // 바인딩 에러 수집
    private Map<String, String> collectErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacement) -> existing));
    }
}
