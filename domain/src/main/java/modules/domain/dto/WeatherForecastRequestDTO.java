package modules.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherForecastRequestDTO {

    @NotNull(message = "페이지 번호를 입력해주세요.")
    private int pageNo;                 // 페이지 번호
    @NotNull(message = "한 페이지 결과 수를 입력해주세요.")
    private int numOfRows;              // 한 페이지 결과 수
    @NotNull(message = "예보지점 X 좌표를 입력해주세요.")
    private int nx;                     // 예보지점 X 좌표
    @NotNull(message = "예보지점 Y 좌표를 입력해주세요.")
    private int ny;                     // 예보지점 Y 좌표

    @Override
    public String toString() {
        return "WeatherForecastRequestDTO{" +
                "pageNo=" + pageNo +
                ", numOfRows=" + numOfRows +
                ", nx=" + nx +
                ", ny=" + ny +
                '}';
    }

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

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }
}
