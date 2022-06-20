package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
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
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 공항철도선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 개화역;
    private StationResponse 김포공항역;

    /**
     *   개화역         교대역     --- *2호선* ---     강남역
     *     |             |                            |
     * *김포공항선*     *3호선*                     *신분당선*
     *     |             |                            |
     *  김포공항역     남부터미널역  --- *3호선* ---     양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        개화역 = StationAcceptanceTest.지하철역_등록되어_있음("개화역").as(StationResponse.class);
        김포공항역 = StationAcceptanceTest.지하철역_등록되어_있음("김포공항역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        공항철도선 = 지하철_노선_등록되어_있음(new LineRequest("육호선", "bg-sky-600", 개화역.getId(), 김포공항역.getId(), 5)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Background
     *
     * Given 지하철 역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철 역 등록되어 있음
     *
     * Scenario: 최단 경로 탐색
     *
     * When 두 역 사이의 최단 경로 탐색 요청
     * Then 최단 경로 조회됨
     *
     */
    @Test
    @DisplayName("출발역, 도착역을 통해 최단경로를 조회한다.")
    void searchShortestPath() {
        // when
        ExtractableResponse<Response> 지하철_경로_조회_요청_결과 = 최단_경로_조회_요청(강남역, 남부터미널역);

        // then
        최단_경로_조회_확인(지하철_경로_조회_요청_결과, 12, Arrays.asList(강남역, 양재역, 남부터미널역));
    }

    /**
     * Background
     *
     * Given 지하철 역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철 역 등록되어 있음
     *
     * Scenario: 최단 경로 탐색 예외발생
     * When 출발역과 도착역이 동일한 경우 최단 경로 조회 요청
     * Then 최단 경로 조회 불가
     *
     * When 출발역과 도착역 사이에 연결되는 경로가 없는 경우 최단 경로 조회 요청
     * Then 최단 경로 조회 불가
     *
     * When 출발역이나 도착역이 존재하지 않는 지하철역인 경우 최단 경로 조회 요청
     * Then 최단 경로 조회 불가
     */
    @Test
    @DisplayName("최단 경로 탐색 불가")
    void shortestPathException() {
        ExtractableResponse<Response> 최단_경로_조회_결과 = 최단_경로_조회_요청(양재역, 양재역);
        최단_경로_조회_불가(최단_경로_조회_결과);

        ExtractableResponse<Response> 연결되어_있지_않은_역_최단_경로_조회_결과 = 최단_경로_조회_요청(양재역, 개화역);
        최단_경로_조회_불가(연결되어_있지_않은_역_최단_경로_조회_결과);

        StationResponse 마곡나루역 = new StationResponse(99L, "마곡나루역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> 존재하지_않는_역_최단_경로_조회_결과 = 최단_경로_조회_요청(마곡나루역, 양재역);
        최단_경로_조회_불가(존재하지_않는_역_최단_경로_조회_결과);
    }

    private void 최단_경로_조회_불가(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source.getId());
        params.put("target", target.getId());

        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .params(params)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    private void 최단_경로_조회_확인(ExtractableResponse<Response> response, int distance, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations()
            .stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
            () -> assertThat(stationIds).containsExactlyElementsOf(expectedStationIds)
        );
    }
}
