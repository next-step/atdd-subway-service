package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 길음역;
    private Long 존재하지_않는_역_ID = 100L;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        길음역 = StationAcceptanceTest.지하철역_등록되어_있음("길음역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 길음역, 사당역, 10);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void 최단경로를_조회한다() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("source", 강남역.getId())
                .param("target", 남부터미널역.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "남부터미널역"));
        assertThat(pathResponse.getDistance()).isEqualTo(12);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void 출발역과_도착역이_같은_경우() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("source", 강남역.getId())
                .param("target", 강남역.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("source", 강남역.getId())
                .param("target", 길음역.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않은 출발역을 조회 할 경우")
    @Test
    void 존재하지_않은_출발역을_조회_할_경우() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("source", 존재하지_않는_역_ID)
                .param("target", 강남역.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않은 도착역을 조회 할 경우")
    @Test
    void 존재하지_않은_도착역을_조회_할_경우() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("source", 강남역.getId())
                .param("target", 존재하지_않는_역_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private LineResponse 지하철_노선_등록되어_있음(
            String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(name, color, upStation.getId(), downStation.getId(), distance)).as(LineResponse.class);
    }
}
