package modules.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherForecastRequestDTO {

    @NotNull(message = "페이지 번호를 입력해주세요.")
    @Min(value = 1, message = "페이지 번호 값은 최소 1입니다.")
    private Integer pageNo;                 // 페이지 번호
    @NotNull(message = "한 페이지 결과 수를 입력해주세요.")
    @Min(value = 1, message = "한 페이지 결과 수는 최소 1입니다.")
    private Integer numOfRows;              // 한 페이지 결과 수
    @NotNull(message = "예보지점 X 좌표를 입력해주세요.")
    private Integer nx;                     // 예보지점 X 좌표
    @NotNull(message = "예보지점 Y 좌표를 입력해주세요.")
    private Integer ny;                     // 예보지점 Y 좌표

    public WeatherForecastRequestDTO(Integer pageNo, Integer numOfRows, Integer nx, Integer ny) {
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.nx = nx;
        this.ny = ny;
    }

    public WeatherForecastRequestDTO() {
    }

    @Override
    public String toString() {
        return "WeatherForecastRequestDTO{" +
                "pageNo=" + pageNo +
                ", numOfRows=" + numOfRows +
                ", nx=" + nx +
                ", ny=" + ny +
                '}';
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Integer numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Integer getNx() {
        return nx;
    }

    public void setNx(Integer nx) {
        this.nx = nx;
    }

    public Integer getNy() {
        return ny;
    }

    public void setNy(Integer ny) {
        this.ny = ny;
    }
}
