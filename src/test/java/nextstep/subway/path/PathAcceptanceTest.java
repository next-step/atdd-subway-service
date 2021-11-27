package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;



@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;


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

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                        new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)
                        ).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                         new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)
                        ).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                        new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)
                        ).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void getShortestPath() {

        ExtractableResponse<Response> response = 최단_경로를_조회(강남역.getId(), 남부터미널역.getId());
        최단_경로를_조회하여_비교(response, 12);
    }

    private ExtractableResponse<Response> 최단_경로를_조회(long source, long target) {
        return RestAssured
                .given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단_경로를_조회하여_비교(ExtractableResponse response, int distance) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
        return 지하철_노선_생성_요청(params);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                extract();
    }


    private ExtractableResponse<Response> 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name);
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}
