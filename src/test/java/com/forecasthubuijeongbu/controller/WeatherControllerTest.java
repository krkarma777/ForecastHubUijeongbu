package com.forecasthubuijeongbu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecasthubuijeongbu.TestConfiguration;
import modules.application.inquireapplication.controller.WeatherController;
import modules.application.syncapplication.service.WeatherService;
import modules.domain.dto.WeatherForecastRequestDTO;
import modules.domain.entity.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        WeatherController weatherController = new WeatherController(weatherService);

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    /**
     * 유효한 요청으로 날씨 데이터를 가져오고 저장하는 테스트
     */
    @Test
    public void testFetchAndSaveForecast_ValidRequest() throws Exception {
        WeatherForecastRequestDTO validRequest = new WeatherForecastRequestDTO(1, 10, 55, 127);

        mockMvc.perform(post("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());

        verify(weatherService, times(1)).fetchWeatherData(any(WeatherForecastRequestDTO.class));
    }

    /**
     * 잘못된 요청으로 날씨 데이터를 가져오고 저장하는 테스트
     */
    @Test
    public void testFetchAndSaveForecast_InvalidRequest() throws Exception {
        WeatherForecastRequestDTO invalidRequest = new WeatherForecastRequestDTO(null, null, null, null);

        mockMvc.perform(post("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pageNo").value("페이지 번호를 입력해주세요."))
                .andExpect(jsonPath("$.numOfRows").value("한 페이지 결과 수를 입력해주세요."))
                .andExpect(jsonPath("$.nx").value("예보지점 X 좌표를 입력해주세요."))
                .andExpect(jsonPath("$.ny").value("예보지점 Y 좌표를 입력해주세요."));
    }

    /**
     * 유효한 좌표로 날씨 데이터를 조회하는 테스트
     */
    @Test
    public void testGetForecasts_ValidCoordinates() throws Exception {
        List<WeatherForecast> forecasts = Collections.singletonList(new WeatherForecast("2020-01-01", "1200", "Temp", "2020-01-01", "1300", "15", 55, 127));
        when(weatherService.getForecasts(55, 127)).thenReturn(forecasts);

        mockMvc.perform(get("/api/forecasts?nx=55&ny=127"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fcstValue").value("15"));
    }

    /**
     * 좌표를 지정하지 않고 날씨 데이터를 조회하는 테스트
     */
    @Test
    public void testGetForecasts_NoCoordinates() throws Exception {
        mockMvc.perform(get("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("좌표를 정확히 입력해주십시오."));
    }

    /**
     * 조회 결과가 없는 경우의 테스트
     */
    @Test
    public void testGetForecasts_EmptyResponse() throws Exception {
        when(weatherService.getForecasts(anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/forecasts?nx=10&ny=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
