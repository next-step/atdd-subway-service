package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 경로 쪼회")
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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역")
                .as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역")
                .as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역")
                .as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역")
                .as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 교대역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 교대역, 5);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 7);
    }

    /**
     * Given 2개 이상의 지하철 역이 등록 되어 있음
     * And 한 개 이상의 지하철 노선이 등록 되어 있음
     * And 지하철 노선에 지하철 역이 등록 되어 있음
     * When 출발역과 도착역을 선택하면
     * Then 최단거리가 조회된다.
     */
    @Test
    @DisplayName("최단경로 조회")
    void 최단경로조회() {
        // when
        Map<String, String> params = new HashMap<>();

        ExtractableResponse<Response> response = 최단경로조회_요청(params);

        // then
        ShortestPathResponse shortestPathResponse = response.as(ShortestPathResponse.class);
        ShortestPathResponse answerPath = new ShortestPathResponse();
        최단경로조회_검증(shortestPathResponse, answerPath);
    }

    private ExtractableResponse<Response> 최단경로조회_요청(Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().queryParams(params).get("/path/short")
                .then().log().all()
                .extract();

        return response;
    }

    private void 최단경로조회_검증(ShortestPathResponse shortestPathResponse, ShortestPathResponse answerPath) {
        assertThat(shortestPathResponse.getDistance()).isEqualTo(answerPath.getDistance());
    }
}
