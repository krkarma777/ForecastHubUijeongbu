package modules.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherForecastCreateResponseDTO {
    private String resultCode;  // 결과코드
    private String resultMsg;   // 결과메시지
    private int numOfRows;      // 한 페이지 결과 수
    private int pageNo;         // 페이지 번호
    private int totalCount;     // 전체 결과 수
    private String dataType;    // 데이터 타입 (XML/JSON)
    private String baseDate;    // 발표일자
    private String baseTime;    // 발표시각
    private String fcstDate;    // 예보일자
    private String fcstTime;    // 예보시각
    private String category;    // 자료구분문자
    private String fcstValue;   // 예보 값
    private int nx;             // 예보지점 X 좌표
    private int ny;             // 예보지점 Y 좌표
}