package modules.domain.entity;

import jakarta.persistence.*;

import java.io.Serializable;

// 날씨 예보 엔티티 클래스
@Entity
@Table(name = "weather_forecasts")
public class WeatherForecast implements Serializable {

    // 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기준 날짜
    @Column(name = "base_date")
    private String baseDate;

    // 기준 시간
    @Column(name = "base_time")
    private String baseTime;

    // 카테고리
    @Column(name = "category")
    private String category;

    // 예보 날짜
    @Column(name = "fcst_date")
    private String fcstDate;

    // 예보 시간
    @Column(name = "fcst_time")
    private String fcstTime;

    // 예보 값
    @Column(name = "fcst_value")
    private String fcstValue;

    // x 좌표
    @Column(name = "nx")
    private int nx;

    // y 좌표
    @Column(name = "ny")
    private int ny;

    // 기본 생성자
    public WeatherForecast() {}

    // 생성자
    public WeatherForecast(String baseDate, String baseTime, String category, String fcstDate, String fcstTime, String fcstValue, int nx, int ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.category = category;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
        this.nx = nx;
        this.ny = ny;
    }

    // 아이디 Getter
    public Long getId() {
        return id;
    }

    // 아이디 Setter
    public void setId(Long id) {
        this.id = id;
    }

    // 기준 날짜 Getter
    public String getBaseDate() {
        return baseDate;
    }

    // 기준 날짜 Setter
    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    // 기준 시간 Getter
    public String getBaseTime() {
        return baseTime;
    }

    // 기준 시간 Setter
    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    // 카테고리 Getter
    public String getCategory() {
        return category;
    }

    // 카테고리 Setter
    public void setCategory(String category) {
        this.category = category;
    }

    // 예보 날짜 Getter
    public String getFcstDate() {
        return fcstDate;
    }

    // 예보 날짜 Setter
    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    // 예보 시간 Getter
    public String getFcstTime() {
        return fcstTime;
    }

    // 예보 시간 Setter
    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    // 예보 값 Getter
    public String getFcstValue() {
        return fcstValue;
    }

    // 예보 값 Setter
    public void setFcstValue(String fcstValue) {
        this.fcstValue = fcstValue;
    }

    // x 좌표 Getter
    public int getNx() {
        return nx;
    }

    // x 좌표 Setter
    public void setNx(int nx) {
        this.nx = nx;
    }

    // y 좌표 Getter
    public int getNy() {
        return ny;
    }

    // y 좌표 Setter
    public void setNy(int ny) {
        this.ny = ny;
    }

    // 문자열 표현 반환
    @Override
    public String toString() {
        return "WeatherForecast{" +
                "id=" + id +
                ", baseDate='" + baseDate + '\'' +
                ", baseTime='" + baseTime + '\'' +
                ", category='" + category + '\'' +
                ", fcstDate='" + fcstDate + '\'' +
                ", fcstTime='" + fcstTime + '\'' +
                ", fcstValue='" + fcstValue + '\'' +
                ", nx=" + nx +
                ", ny=" + ny +
                '}';
    }
}
