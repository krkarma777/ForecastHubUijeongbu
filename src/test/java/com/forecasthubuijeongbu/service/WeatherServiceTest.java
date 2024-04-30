package com.forecasthubuijeongbu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.forecasthubuijeongbu.TestConfiguration;
import modules.application.syncapplication.service.WeatherService;
import modules.domain.dto.WeatherForecastRequestDTO;
import modules.domain.entity.WeatherForecast;
import modules.domain.repository.WeatherForecastRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(WeatherService.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @MockBean
    private WeatherForecastRepository weatherForecastRepository;

    @InjectMocks
    private WeatherService weatherService;

    @Autowired
    private Environment env;

    private final String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    private final String apiKey = "7bcR435Oy54aLzeQQR58s936arOoB80TPOmZ4kpHvju7cKvXw8pV8GU0EMhb4C5NjaAkpB55i6WNM5Ccs6AHsA==";

    @BeforeEach
    public void setup() {
        weatherService = new WeatherService(restTemplate, weatherForecastRepository, mock(ObjectMapper.class), baseUrl, apiKey);
    }

    @Test
    public void testFetchWeatherData() {
        WeatherForecastRequestDTO requestDTO = new WeatherForecastRequestDTO();
        requestDTO.setNx(55);
        requestDTO.setNy(127);

        System.out.println("baseUrl = " + baseUrl);
        System.out.println("apiKey = " + apiKey);

        String mockResponse = "{\"response\":\"data\"}";
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<String> results = weatherService.fetchWeatherData(requestDTO);

        verify(restTemplate, times(32)).getForEntity(anyString(), eq(String.class));  // 각 시간 슬롯에 대한 호출 확인
        assertEquals(32, results.size());  // 4일 각각 8시간 슬롯 데이터 확인
    }

    @Test
    public void testSaveForecastData() throws JsonProcessingException {
        String json = "{\"response\": {\"body\": {\"items\": {\"item\": [{\"baseDate\": \"20200401\", \"baseTime\": \"1200\", \"nx\": 55, \"ny\": 127, \"category\": \"TMP\", \"fcstValue\": \"20\"}]}}}}";

        ObjectMapper mockMapper = mock(ObjectMapper.class);
        JsonNode mockNode = mock(JsonNode.class);
        JsonNode mockItems = mock(JsonNode.class);
        JsonNode mockItemArray = mock(JsonNode.class);
        JsonNode mockItem = mock(JsonNode.class);

        when(mockMapper.readTree(json)).thenReturn(mockNode);
        when(mockNode.path("response")).thenReturn(mockNode);
        when(mockNode.path("body")).thenReturn(mockNode);
        when(mockNode.path("items")).thenReturn(mockItems);
        when(mockItems.path("item")).thenReturn(mockItemArray);
        when(mockItemArray.get(0)).thenReturn(mockItem);

        // JsonNode에서 각 필드 값을 가져오는 부분도 모의 처리
        when(mockItem.path("baseDate")).thenReturn(new TextNode("20200401"));
        when(mockItem.path("baseTime")).thenReturn(new TextNode("1200"));
        when(mockItem.path("nx")).thenReturn(new IntNode(55));
        when(mockItem.path("ny")).thenReturn(new IntNode(127));
        when(mockItem.path("category")).thenReturn(new TextNode("TMP"));
        when(mockItem.path("fcstValue")).thenReturn(new TextNode("20"));

        WeatherService service = new WeatherService(null, weatherForecastRepository, mockMapper);

        service.saveForecastData(json);
    }

    @Test
    public void testGetForecasts() {
        int nx = 55, ny = 127;
        List<WeatherForecast> mockForecasts = Collections.singletonList(new WeatherForecast());
        when(weatherForecastRepository.findByNxAndNy(nx, ny)).thenReturn(mockForecasts);

        List<WeatherForecast> results = weatherService.getForecasts(nx, ny);

        assertEquals(mockForecasts, results);
    }
}
