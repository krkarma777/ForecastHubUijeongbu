package modules.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherForecastRequestDTO {
    private String serviceKey;          // 공공데이터포털에서 받은 인증키
    private int pageNo;                 // 페이지 번호
    private int numOfRows;              // 한 페이지 결과 수
    private String dataType = "JSON";   // 응답 자료 형식 (XML/JSON)
    private String baseDate;            // 발표일자
    private String baseTime;            // 발표시각
    private int nx;                     // 예보지점 X 좌표
    private int ny;                     // 예보지점 Y 좌표
}
