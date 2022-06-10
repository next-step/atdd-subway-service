package nextstep.subway.path.acceptance;

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

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;


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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    // 최단경로 조회
    @DisplayName("최단경로 조회")
    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> 최단경로_조회_결과 = 최단경로_조회(교대역, 양재역);

        // then
        최단경로_조회됨(최단경로_조회_결과);
    }

    @DisplayName("최단경로 조회 실패 시나리오")
    @Test
    public void findPath_fail_scenario() throws Exception {
        // given
        StationResponse 존재하지않는역 = StationAcceptanceTest.지하철역_등록되어_있음("존재하지않는역").as(StationResponse.class);
        
        // when
        ExtractableResponse<Response> 출발역과_도착역이_같은_최단경로_조회_결과 = 최단경로_조회(교대역, 교대역);
        // Then 조회 요청이 실패됨
        최단경로_조회_실패됨(출발역과_도착역이_같은_최단경로_조회_결과);

        // when
        ExtractableResponse<Response> 출발역과_도착역이_연결되어_있지않은_최단경로_조회_결과 = 최단경로_조회(강남역, 남부터미널역);
        // Then 조회 요청이 실패됨
        최단경로_조회_실패됨(출발역과_도착역이_같은_최단경로_조회_결과);

        // when
        ExtractableResponse<Response> 존재하지_않는_역_최단경로_조회_결과 = 최단경로_조회(교대역, 존재하지않는역);
        // Then 조회 요청이 실패됨
        최단경로_조회_실패됨(출발역과_도착역이_같은_최단경로_조회_결과);
    }

    private  ExtractableResponse<Response> 최단경로_조회(StationResponse 출발역, StationResponse 도착역) {
        return null;
    }

    private void 최단경로_조회됨(ExtractableResponse<Response> response) {
    }

    private void 최단경로_조회_실패됨(ExtractableResponse<Response> response) {
    }
}
