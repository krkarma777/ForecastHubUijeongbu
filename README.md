### 프로젝트 설명

이 저장소는 Spring Boot를 사용하여 개발된 날씨 예보 정보 제공 서비스입니다. 사용자는 지정된 좌표(nx, ny)에 대한 날씨 정보를 조회하거나 저장할 수 있습니다. 데이터는 외부 날씨 API를 통해 수집되며, 중복 데이터를 피하기 위해 저장 전 데이터베이스에서 중복 검사를 수행합니다. 이 프로젝트는 멀티 모듈 구조로 설계되어 있으며, 각 모듈은 도메인, 애플리케이션, 공통 구성 요소 등 다양한 측면을 다룹니다.

### 주요 기능

1. **날씨 데이터 수집 및 저장**: `POST /api/forecasts` 엔드포인트를 통해 외부 API로부터 날씨 데이터를 수집하고 데이터베이스에 저장합니다.
2. **날씨 정보 조회**: `GET /api/forecasts` 엔드포인트를 사용하여 데이터베이스에 저장된 날씨 정보를 조회할 수 있습니다. 조회는 nx, ny 좌표를 기준으로 합니다.

### 구성 요소

- **WeatherController**: 사용자 요청을 처리하고 응답을 관리하는 REST 컨트롤러.
- **WeatherService**: 외부 날씨 API와의 통신 및 데이터베이스 작업을 관리합니다.
- **WeatherForecastRepository**: JPA를 사용하여 날씨 예보 데이터에 대한 CRUD 작업을 수행합니다.
- **WeatherForecast**: 날씨 정보를 저장하는 엔티티 클래스.
- **WeatherForecastRequestDTO**: 클라이언트 요청을 담는 데이터 전송 객체.

### 기술 스택

- **Spring Boot**: 웹 애플리케이션 개발 프레임워크
- **Spring Data JPA**: 데이터베이스 작업을 위한 ORM
- **Jackson**: JSON 데이터 바인딩을 위한 라이브러리
- **RestTemplate**: 외부 API 호출을 위한 스프링의 HTTP 클라이언트

### 시작 방법

1. 프로젝트 클론하기:
   ```
   git clone https://github.com/krkarma777/ForecastHubUijeongbu.git
   ```
2. 프로젝트 디렉토리로 이동:
   ```
   cd ForecastHubUijeongbu
   ```
3. `application-dev.properties` 파일을 생성하고 API 키 및 URL을 설정:
   ```
   weather.api.key=YOUR_API_KEY
   weather.api.url=http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst
   ```
4. 애플리케이션 실행:
   ```
   ./mvnw spring-boot:run
   ```

### API 사용 예 / 경기도 의정부시 문충로74 지역

- #### 날씨 데이터 저장

```bash
curl -X POST "http://localhost:8080/api/forecasts" -H "Content-Type: application/json" -d '{"pageNo": 1, "numOfRows": 10, "nx": 55, "ny": 127}'
```

포스트맨 사용시

```
{
  "pageNo": 1,
  "numOfRows": 100,
  "nx": 55,
  "ny": 127
}
```

- #### 날씨 정보 조회

```bash
curl -X GET "http://localhost:8080/api/forecasts?nx=55&ny=127"
```

포스트맨 사용시

```
http://localhost:8090/api/forecasts?nx=55&ny=127
```
