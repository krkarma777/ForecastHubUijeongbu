package modules.application.syncapplication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import modules.domain.dto.WeatherForecastRequestDTO;
import modules.domain.entity.WeatherForecast;
import modules.domain.repository.WeatherForecastRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String baseUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final WeatherForecastRepository weatherForecastRepository;
    private final ObjectMapper objectMapper;

    public WeatherService(RestTemplate restTemplate, WeatherForecastRepository weatherForecastRepository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.weatherForecastRepository = weatherForecastRepository;
        this.objectMapper = objectMapper;
    }

    // 요청된 파라미터와 오늘 포함 지난 3일간의 날씨 데이터를 가져옵니다.
    public List<String> fetchWeatherData(WeatherForecastRequestDTO requestDTO) {
        LocalDate today = LocalDate.now();
        List<String> results = new ArrayList<>();
        String[] times = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

        for (String time : times) {
            // 해당 날짜와 시간에 대한 데이터를 가져옵니다.
            results.addAll(fetchDataForTimeSlot(today, time, requestDTO));
        }

        return results;
    }

    // 지정된 날짜와 시간에 대해 날씨 데이터를 가져옵니다.
    private List<String> fetchDataForTimeSlot(LocalDate date, String time, WeatherForecastRequestDTO requestDTO) {
        List<String> data = new ArrayList<>();
        data.add(fetchWeatherDataForDate(date, time, requestDTO));
        for (int i = 1; i <= 3; i++) {
            LocalDate previousDate = date.minusDays(i);
            data.add(fetchWeatherDataForDate(previousDate, time, requestDTO));
        }
        return data;
    }

    // 구축된 URL을 사용하여 날씨 데이터를 가져옵니다.
    private String fetchWeatherDataForDate(LocalDate date, String time, WeatherForecastRequestDTO requestDTO) {
        String url = buildUrl(date.format(DateTimeFormatter.BASIC_ISO_DATE), time, requestDTO);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    // 데이터베이스에 없는 경우에만 날씨 데이터를 저장합니다.
    public void saveForecastData(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");
            items.forEach(this::saveForecastIfNotExists);
        } catch (IOException e) {
            // 예외 처리 로그
        }
    }

    // 데이터베이스에 이미 데이터가 있는지 확인합니다.
    private void saveForecastIfNotExists(JsonNode item) {
        if (!existsInDatabase(item)) {
            weatherForecastRepository.save(parseForecast(item));
        }
    }

    private boolean existsInDatabase(JsonNode item) {
        return weatherForecastRepository.existsByBaseDateAndBaseTimeAndNxAndNyAndCategory(
                item.path("baseDate").asText(), item.path("baseTime").asText(),
                item.path("nx").asInt(), item.path("ny").asInt(), item.path("category").asText());
    }

    // 날씨 예보 데이터를 파싱합니다.
    private WeatherForecast parseForecast(JsonNode item) {
        return new WeatherForecast(
                item.path("baseDate").asText(),
                item.path("baseTime").asText(),
                item.path("category").asText(),
                item.path("fcstDate").asText(),
                item.path("fcstTime").asText(),
                item.path("fcstValue").asText(),
                item.path("nx").asInt(),
                item.path("ny").asInt()
        );
    }

    // 요청된 데이터에 따라 URL을 구축합니다.
    private String buildUrl(String baseDate, String baseTime, WeatherForecastRequestDTO requestDTO) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("pageNo", requestDTO.getPageNo())
                .queryParam("numOfRows", requestDTO.getNumOfRows())
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", requestDTO.getNx())
                .queryParam("ny", requestDTO.getNy())
                .build().toString();
    }

    // 지정된 위치(nx, ny)에 대한 날씨 예보를 가져옵니다.
    public List<WeatherForecast> getForecasts(int nx, int ny) {
        return weatherForecastRepository.findByNxAndNy(nx, ny);
    }
}
