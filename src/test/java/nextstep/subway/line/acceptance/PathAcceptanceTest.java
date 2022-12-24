package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.fee.dto.FeeRequest;
import nextstep.subway.fee.dto.FeeResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_요금_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 구간 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 노량진역;

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
        노량진역 = StationAcceptanceTest.지하철역_등록되어_있음("노량진역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 15);

        지하철_기본요금_등록되어_있음(11, 50, 5, 100);
        지하철_기본요금_등록되어_있음(51, 999999, 8, 100);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest params = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_생성_요청(params).body()
                .jsonPath()
                .getObject(".", LineResponse.class);
    }

    private FeeResponse 지하철_기본요금_등록되어_있음(int applyStartDistance, int applyEndDistance, int applyDistance, int applyFee) {
        FeeRequest params = new FeeRequest(applyStartDistance, applyEndDistance, applyDistance, applyFee);
        return 지하철_요금_생성_요청(params).body()
                .jsonPath()
                .getObject(".", FeeResponse.class);
    }

    /**
     * Given 출발역과 도착역을 받으면
     * When 다익스트라 알고리즘으로 최적의 경우를 구한후
     * Then 최적의 경로를 돌려준다.
     */
    @Test
    @DisplayName("최적경로 조회")
    void getShortestPath() {
        Map<String, Integer> params = 지하철노선_최적경로_조회_세팅됨(3L, 2L);

        // Assert를 이용해 요청을 보내고
        ExtractableResponse<Response> extract = 지하철역_최적경로_조회(params);

        // 응답값을 받는다
        지하철노선_최적경로_응답됨(extract);
    }

    /**
     * Given 출발역과 도착역을 받으면
     * When 다익스트라 알고리즘으로 최적의 경우를 구한후
     * Then 최적의 경로를 돌려준다.
     */
    @Test
    @DisplayName("최적경로 조회 - 출발역과 도착역이 같은 경우 실패 테스트")
    void getShortestPath2() {
        Map<String, Integer> params = 지하철노선_최적경로_조회_세팅됨(3L, 3L);

        ExtractableResponse<Response> response = 지하철역_최적경로_조회(params);

        지하철_최적_경로_조회_실패됨(response);
    }

    /**
     * Given 출발역과 도착역을 받으면
     * When 다익스트라 알고리즘으로 최적의 경우를 구한후
     * Then 최적의 경로를 돌려준다.
     */
    @Test
    @DisplayName("최적경로 조회 - 출발역과 도착역이 연결이 되지 않은 경우 실패 테스트")
    void getShortestPath3() {
        Map<String, Integer> params = 지하철노선_최적경로_조회_세팅됨(3L, 5L);

        ExtractableResponse<Response> response = 지하철역_최적경로_조회(params);

        지하철_최적_경로_조회_실패됨(response);
    }

    /**
     * Given 출발역과 도착역을 받으면
     * When 다익스트라 알고리즘으로 최적의 경우를 구한후
     * Then 최적의 경로를 돌려준다.
     */
    @Test
    @DisplayName("최적경로 조회 - 존재하지 않은 출발역이나 도착역을 조회할 경우 실패 테스트")
    void getShortestPath4() {
        Map<String, Integer> params = 지하철노선_최적경로_조회_세팅됨(3L, 10L);

        ExtractableResponse<Response> response = 지하철역_최적경로_조회(params);

        지하철_최적_경로_조회_실패됨(response);
    }

    /**
     * Given 출발역과 도착역을 받으면
     * When 다익스트라 알고리즘으로 최적의 경우, 거리, 금액을 구한 후
     * Then 지하철 경로 정보를 돌려준다.
     */
    @Test
    @DisplayName("지하철역 조회 - 거리, 금액 포함하도록 조회 - 기본 요금")
    void getStationPathInfo() {
        Map<String, Integer> params = 지하철노선_최적경로_조회_세팅됨(3L, 2L);
        int totalDistance = 15;
        int totalFee = 1350;

        ExtractableResponse<Response> extract = 지하철역_경로정보_조회(params);

        지하철노선_경로정보_응답됨(extract, totalDistance, totalFee);
    }

    private Map 지하철노선_최적경로_조회_세팅됨(Long source, Long target) {
        Map params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return params;
    }

    private static void 지하철노선_최적경로_응답됨(ExtractableResponse<Response> extract) {
        assertThat(extract.jsonPath().getList("name").containsAll(Arrays.asList("교대역", "남부터미널역", "양재역"))).isTrue();
    }

    private static void 지하철노선_경로정보_응답됨(ExtractableResponse<Response> extract, int totalDistance, int totalFee) {
        assertThat(extract.jsonPath().get("totalDistance").equals(totalDistance)).isTrue();
        assertThat(extract.jsonPath().get("totalFee").equals(totalFee)).isTrue();
    }

    public static void 지하철_최적_경로_조회_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 지하철역_최적경로_조회(Map<String, Integer> params) {
        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params).when().get("/shortestPath")
                .then().log().all()
                .extract();
        return extract;
    }

    private static ExtractableResponse<Response> 지하철역_경로정보_조회(Map<String, Integer> params) {
        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params).when().get("/paths")
                .then().log().all()
                .extract();
        return extract;
    }
}
