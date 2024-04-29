package com.forecasthubuijeongbu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecasthubuijeongbu.TestConfiguration;
import modules.application.inquireapplication.controller.WeatherController;
import modules.application.syncapplication.service.WeatherService;
import modules.domain.dto.WeatherForecastRequestDTO;
import modules.domain.entity.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        weatherController = new WeatherController(mock(WeatherService.class));

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testFetchAndSaveForecast_ValidRequest() throws Exception {
        // 유효한 요청에 대한 테스트
        WeatherForecastRequestDTO validRequest = new WeatherForecastRequestDTO(1, 10, 55, 127);

        mockMvc.perform(post("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());

        verify(weatherService, times(1)).fetchWeatherData(any(WeatherForecastRequestDTO.class));
    }

    @Test
    public void testFetchAndSaveForecast_InvalidRequest() throws Exception {
        // 잘못된 요청에 대한 테스트
        WeatherForecastRequestDTO invalidRequest = new WeatherForecastRequestDTO(null, null, null, null);

        mockMvc.perform(post("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())  // HTTP 400 상태 코드 검증
                .andExpect(jsonPath("$.pageNo").value("페이지 번호를 입력해주세요."))
                .andExpect(jsonPath("$.numOfRows").value("한 페이지 결과 수를 입력해주세요."))
                .andExpect(jsonPath("$.nx").value("예보지점 X 좌표를 입력해주세요."))
                .andExpect(jsonPath("$.ny").value("예보지점 Y 좌표를 입력해주세요."));
    }

    @Test
    public void testGetForecasts_ValidCoordinates() throws Exception {
        // 유효한 좌표로 예보 요청
        when(weatherService.getForecasts(55, 127)).thenReturn(Collections.singletonList(new WeatherForecast()));

        mockMvc.perform(get("/api/forecasts?nx=55&ny=127")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetForecasts_NoCoordinates() throws Exception {
        // 좌표 없이 예보 요청
        mockMvc.perform(get("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("좌표를 정확히 입력해주십시오."));
    }

    @Test
    public void testGetForecasts_EmptyResponse() throws Exception {
        // 결과가 없는 경우
        when(weatherService.getForecasts(anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/forecasts?nx=10&ny=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}