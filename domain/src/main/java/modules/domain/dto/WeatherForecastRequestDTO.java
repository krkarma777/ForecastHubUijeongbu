package modules.domain.dto;

public class WeatherForecastRequestDTO {
    private int pageNo;                 // 페이지 번호
    private int numOfRows;              // 한 페이지 결과 수
    private String dataType = "JSON";   // 응답 자료 형식 (XML/JSON)
    private String baseDate;            // 발표일자
    private String baseTime;            // 발표시각
    private Double nx;                     // 예보지점 X 좌표
    private Double ny;                     // 예보지점 Y 좌표

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public Double getNx() {
        return nx;
    }

    public void setNx(Double nx) {
        this.nx = nx;
    }

    public Double getNy() {
        return ny;
    }

    public void setNy(Double ny) {
        this.ny = ny;
    }
}
