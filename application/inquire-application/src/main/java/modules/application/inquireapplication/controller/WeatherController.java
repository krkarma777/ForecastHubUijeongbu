package modules.application.inquireapplication.controller;

import modules.application.syncapplication.service.WeatherService;
import modules.domain.dto.WeatherForecastRequestDTO;
import modules.domain.entity.WeatherForecast;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/forecasts")
    public ResponseEntity<?> fetchAndSaveForecast(@RequestBody  @Validated WeatherForecastRequestDTO requestDTO,
                                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getErrors(bindingResult);
            String s = errors.values().stream().toList().get(0);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", s, "errors", errors));
        }
        weatherService.fetchWeatherData(requestDTO).forEach(weatherService::saveForecastData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/forecasts")
    public ResponseEntity<List<WeatherForecast>> getForecasts(@RequestParam int nx, @RequestParam int ny) {
        List<WeatherForecast> forecasts = weatherService.getForecasts(nx, ny);
        if (forecasts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(forecasts);
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
