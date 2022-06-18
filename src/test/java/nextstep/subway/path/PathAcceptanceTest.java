package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineAcceptanceMethod.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethod.*;
import static nextstep.subway.station.StationAcceptanceMethod.*;
import static nextstep.subway.path.PathAcceptanceMethod.*;

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
     * 교대역    --- *2호선*(10) ---  강남역
     * |                            |
     * *3호선*(3)                   *신분당선*(10)
     * |                            |
     * 남부터미널역 --- *3호선*(2) --- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(LineRequest.of("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * When 교대역부터 양재역까지의 최단 경로를 조회하면
     * Then 최단 경로가 조회된다 (교대역 -(3)-> 남부터미널역 -(2)-> 양재역)
     */
    @DisplayName("교대역부터 양재역까지의 최단 경로를 조회한다.")
    @Test
    void find_shortest_path_01() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(교대역, 남부터미널역, 양재역));
    }

    /**
     * When 남부터미널역부터 강남역까지의 최단 경로를 조회하면
     * Then 최단 경로가 조회된다 (남부터미널역 -(2)-> 양재역 -(10)-> 강남역)
     */
    @DisplayName("남부터미널역부터 강남역까지의 최단 경로를 조회한다.")
    @Test
    void find_shortest_path_02() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(남부터미널역.getId(), 강남역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(남부터미널역, 양재역, 강남역));
    }

    /**
     * When 출발역과 도착역이 같은 최단 경로를 조회하면
     * Then 경로를 조회할 수 없다
     */
    @DisplayName("출발역과 도착역이 같은 경우 경로를 조회할 수 없다.")
    @Test
    void equals_source_target() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(양재역.getId(), 양재역.getId());

        // then
        지하철_최단경로_조회_실패됨(response);
    }

    /**
     * Given 신규 지하철역을 생성하고 (건대입구역)
     * When 출발역과 도착역이 연결되어 있지 않은 최단 경로를 조회하면 (건대입구역 -> 강남역)
     * Then 경로를 조회할 수 없다
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 경로를 조회할 수 없다.")
    @Test
    void no_connect_source_target() {
        // given
        StationResponse 건대입구역 = 지하철역_등록되어_있음("건대입구역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(건대입구역.getId(), 강남역.getId());

        // then
        지하철_최단경로_조회_실패됨(response);
    }

    /**
     * Given 존재하지 않은 역의 ID
     * When 출발역이 존재하지 않는 최단 경로를 조회하면 (동대구역 -> 강남역)
     * Then 경로를 조회할 수 없다
     */
    @DisplayName("출발역이 존재하지 않는 경우 경로를 조회할 수 없다.")
    @Test
    void not_exist_source() {
        // given
        Long 동대구역_ID = 0L;

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(동대구역_ID, 강남역.getId());

        // then
        지하철_최단경로_조회_실패됨(response);
    }

    /**
     * Given 존재하지 않은 역의 ID
     * When 도착역이 존재하지 않는 최단 경로를 조회하면 (강남역 -> 동대구역)
     * Then 경로를 조회할 수 없다
     */
    @DisplayName("도착역이 존재하지 않는 경우 경로를 조회할 수 없다.")
    @Test
    void not_exist_target() {
        // given
        Long 동대구역_ID = 0L;

        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(강남역.getId(), 동대구역_ID);

        // then
        지하철_최단경로_조회_실패됨(response);
    }
}
