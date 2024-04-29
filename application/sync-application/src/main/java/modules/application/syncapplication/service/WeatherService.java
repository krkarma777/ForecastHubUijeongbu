package modules.application.syncapplication.service;


import modules.domain.dto.WeatherForecastRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String baseUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getWeatherData(WeatherForecastRequestDTO requestDTO) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("pageNo", requestDTO.getPageNo())
                .queryParam("numOfRows", requestDTO.getNumOfRows())
                .queryParam("dataType", requestDTO.getDataType())
                .queryParam("base_date", requestDTO.getBaseDate())
                .queryParam("base_time", requestDTO.getBaseTime())
                .queryParam("nx", requestDTO.getNx())
                .queryParam("ny", requestDTO.getNy())
                .build().encode().toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}