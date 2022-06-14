package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineRestAssured.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionRestAssured.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static nextstep.subway.path.acceptance.PathRestAssured.지하철_경로_최단거리_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
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

/** Feature: 지하철 경로 조회 기능 */
@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 강동역;
    private StationResponse 천호역;

    /**
     * 교대역    --- *2호선* (10) ---   강남역
     * |                             |
     * *3호선* (3)                  *신분당선* (10)
     * |                             |
     * 남부터미널역  --- *3호선* (2) --- 양재
     *
     *  강동역  ---- *5호선* (5) ----  천호역
     *
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 구간들 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        강동역 = StationAcceptanceTest.지하철역_등록되어_있음("강동역").as(StationResponse.class);
        천호역 = StationAcceptanceTest.지하철역_등록되어_있음("천호역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        오호선 = 지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 강동역.getId(), 천호역.getId(), 5)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * When 지하철 경로에 출발지와 목적지로 최단경로를 조회하면
     * Then 최단 거리 경로와 거리가 조회된다.
     */
    @DisplayName("지하철 경로에 출발지와 목적지로 최단경로를 조회하면 입력하면 최단 거리가 조회된다.")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_최단거리_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_경로_최단거리_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
    }

    /**
     * When 지하철 경로에 같은 출발역, 도착역으로 최단경로를 조회하면
     * Then 조회에 실패한다.
     */
    @DisplayName("지하철 경로에 같은 도착역, 목적역으로 최단경로를 조회시 실패 한다.")
    @Test
    void shortestPathWithSameSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_최단거리_요청(교대역.getId(), 교대역.getId());

        // then
        지하철_경로_최단거리_실패됨(response);
    }

    /**
     * When 지하철 경로에 출발역, 도착역이 연결이 되지 않은 상태로 조회하면
     * Then 조회에 실패한다.
     */
    @DisplayName("지하철 경로에 출발역, 도착역이 연결이 되지 않은 상태로 조회시 실패 한다.")
    @Test
    void shortestPathWithUnConnectedSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_최단거리_요청(교대역.getId(), 천호역.getId());

        // then
        지하철_경로_최단거리_실패됨(response);
    }

    /**
     * When 지하철 경로에 존재하지 않는 출발역이나 도착역으로 최단경로를 조회하면
     * Then 조회에 실패한다.
     */
    @DisplayName("지하철 경로에 존재하지 않는 출발역이나 도착역으로 최단경로 조회시 실패 한다.")
    @Test
    void shortestPathWithExcludeStation() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_최단거리_요청(교대역.getId(), createStation(999999L, "홍대입구").id());

        // then
        지하철_경로_최단거리_실패됨(response);
    }

    private void 지하철_경로_최단거리_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations, int distance) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(distance),
                () -> assertThat(stationIds).containsExactlyElementsOf(expectedStationIds)
        );
    }

    private void 지하철_경로_최단거리_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
