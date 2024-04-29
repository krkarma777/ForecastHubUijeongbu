package com.forecasthubuijeongbu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecasthubuijeongbu.TestConfiguration;
import modules.application.inquireapplication.controller.WeatherController;
import modules.application.syncapplication.service.WeatherService;
import modules.domain.dto.WeatherForecastRequestDTO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
        weatherService = mock(WeatherService.class);
        weatherController = new WeatherController(weatherService);

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testFetchAndSaveForecast_ValidRequest() throws Exception {
        WeatherForecastRequestDTO requestDTO = new WeatherForecastRequestDTO();
        requestDTO.setPageNo(1);
        requestDTO.setNumOfRows(10);
        requestDTO.setNx(55);
        requestDTO.setNy(127);

        mockMvc.perform(post("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(weatherService, times(1)).fetchWeatherData(any(WeatherForecastRequestDTO.class));
    }

    @Test
    public void testFetchAndSaveForecast_InvalidRequest() throws Exception {
        // 유효하지 않은 데이터로 구성된 DTO 생성
        WeatherForecastRequestDTO requestDTO = new WeatherForecastRequestDTO();
        requestDTO.setPageNo(null);  // 유효하지 않음
        requestDTO.setNumOfRows(null);  // 유효하지 않음
        requestDTO.setNx(null);  // 유효하지 않음
        requestDTO.setNy(null);  // 유효하지 않음

        // MockMvc를 사용하여 POST 요청을 시뮬레이션
        mockMvc.perform(post("/api/forecasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())  // HTTP 400 상태 코드 검증
                .andExpect(jsonPath("$.pageNo").value("페이지 번호를 입력해주세요."))  // 오류 메시지 검증
                .andExpect(jsonPath("$.numOfRows").value("한 페이지 결과 수를 입력해주세요."))  // 오류 메시지 검증
                .andExpect(jsonPath("$.nx").value("예보지점 X 좌표를 입력해주세요."))  // 오류 메시지 검증
                .andExpect(jsonPath("$.ny").value("예보지점 Y 좌표를 입력해주세요."));  // 오류 메시지 검증
    }
}