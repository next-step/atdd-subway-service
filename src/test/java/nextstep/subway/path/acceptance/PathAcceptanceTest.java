package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
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
     * 교대역  --- *2호선* (10)--   강남역
     * |                        |
     * *3호선* (3)               *신분당선* (10)
     * |                        |
     * 남부터미널역 - *3호선* (2) -  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Feature: 지하철 경로 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음 (강남역, 양재역, 교대역, 남부터미널역)
     *     Given 노선 등록되어 있음 (신분당선, 이호선, 삼호선)
     *
     *   Scenario: 지하철 최단경로 탐색
     *     When 교대역-양재역 최단경로 조회
     *     Then 교대역-남부터미널역-양재역 경로 조회됨
     *     When 강남역-남부터미널역 최단경로 조회
     *     Then 강남역-양재역-남부터미널역 최단 경로 조회됨
     */
    @DisplayName("지하철 경로를 탐색한다.")
    @Test
    void 지하철_경로_탐색_정상_시나리오() {
        ExtractableResponse<Response> 교대역_양재역_조회 = 최단경로_조회_요청(교대역, 양재역);
        최단경로_조회됨(교대역_양재역_조회);
        최단경로_결과_정렬됨(교대역_양재역_조회, Arrays.asList(교대역, 남부터미널역, 양재역));

        ExtractableResponse<Response> 강남역_남부터미널역_조회 = 최단경로_조회_요청(강남역, 남부터미널역);
        최단경로_조회됨(강남역_남부터미널역_조회);
        최단경로_결과_정렬됨(강남역_남부터미널역_조회, Arrays.asList(강남역, 양재역, 남부터미널역));
    }

    /**
     * Feature: 지하철 경로 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음 (강남역, 양재역, 교대역, 남부터미널역, 부평역, 인천시청역)
     *     Given 노선 등록되어 있음 (신분당선, 이호선, 삼호선, 인천호선)
     *
     *   Scenario: 지하철 최단경로 탐색
     *     When 출발역과 도착역을 동일하게 최단경로 조회
     *     Then 경로 조회 실패함
     *     When 출발역과 도착역이 연결되어있지 않은 상태에서 최단경로 조회
     *     Then 경로 조회 실패함
     *     When 존재하지 않는 출발역으로 최단경로 조회
     *     Then 경로 조회 실패함
     *     When 존재하지 않는 도착역으로 최단경로 조회
     *     Then 경로 조회 실패함
     */
    @DisplayName("지하철 구간을 관리 실패한다.")
    @Test
    void 지하철_경로_탐색_비정상_시나리오() {
        //given
        StationResponse 부평역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 인천시청역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        LineResponse 인천호선 = 지하철_노선_등록되어_있음(new LineRequest("인천호선", "bg-skyblue-600", 부평역.getId(), 인천시청역.getId(), 10)).as(LineResponse.class);

        ExtractableResponse<Response> 교대역_교대역_조회 = 최단경로_조회_요청(교대역, 교대역);
        최단경로_조회_실패됨(교대역_교대역_조회);

        ExtractableResponse<Response> 교대역_부평역_조회 = 최단경로_조회_요청(교대역, 부평역);
        최단경로_조회_실패됨(교대역_부평역_조회);

        ExtractableResponse<Response> 교대역_존재하지않는역_조회 = 최단경로_조회_요청(교대역, new StationResponse(0L, "존재하지않는역", LocalDateTime.now(), LocalDateTime.now()));
        최단경로_조회_실패됨(교대역_존재하지않는역_조회);

        ExtractableResponse<Response> 존재하지않는역_교대역_조회 = 최단경로_조회_요청(new StationResponse(0L, "존재하지않는역", LocalDateTime.now(), LocalDateTime.now()), 교대역);
        최단경로_조회_실패됨(존재하지않는역_교대역_조회);
    }

    private static ExtractableResponse<Response> 최단경로_조회_요청(StationResponse fromStation, StationResponse toStation) {
        return null;
    }

    public static void 최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 최단경로_결과_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private static void 최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
