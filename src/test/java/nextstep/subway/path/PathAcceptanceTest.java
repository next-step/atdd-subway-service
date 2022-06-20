package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
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
     * 교대역    --- *2호선* (10) ---   강남역
     * |                        |
     * *3호선* (5)                   *신분당선* (10)
     * |                        |
     * 남부터미널역  --- *3호선* (3) ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);


        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 남부터미널역.getId(), 양재역.getId(), 3)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 5);
    }

    /**
     * GIVEN 노선에 지하철역이 등록되어 있다면
     * WHEN 최단 경로를 조회한다면
     * THEN 결과를 확인한다
     */
    @DisplayName("최단 경로 조회를 성공한다")
    @Test
    void 최단경로_조회_성공() {
        Map<String, String> params = new HashMap<>();
        params.put("source", 교대역.getId().toString());
        params.put("target", 양재역.getId().toString());

        ExtractableResponse<Response> response = 최단경로_조회(params);

        최단경로_조회_성공(response, 8);
    }

    /**
     * GIVEN 노선에 지하철역이 등록되어 있고,
     * WHEN 출발지와 도착지가 같다면,
     * THEN 예외를 던진다
     */
    @Test
    void 최단경로_조회_출발지_도착지_일치_실패() {
        Map<String, String> params = new HashMap<>();
        params.put("source", 교대역.getId().toString());
        params.put("target", 교대역.getId().toString());

        ExtractableResponse<Response> response = 최단경로_조회(params);

        최단경로_조회_실패(response, HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단경로_조회(Map<String, String> params) {
        return RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단경로_조회_성공(ExtractableResponse<Response> response, int distance) {
        int resulDistance = response.jsonPath().get("distance");
        assertThat(resulDistance).isEqualTo(distance);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단경로_조회_실패(ExtractableResponse<Response> response, int httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus);
    }
}
