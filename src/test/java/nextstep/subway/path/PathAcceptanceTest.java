package nextstep.subway.path;

import static nextstep.subway.fare.domain.FareType.*;
import static nextstep.subway.line.acceptance.LineAcceptanceMethods.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethods.*;
import static nextstep.subway.path.PathAcceptanceMethods.*;
import static nextstep.subway.station.StationAcceptanceMethods.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_5 = 5;
    private static final int DISTANCE_3 = 3;
    private static final int DISTANCE_0 = 0;

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

        강남역 = 지하철역_등록되어_있음(StationRequest.of("강남역")).as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음(StationRequest.of("양재역")).as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음(StationRequest.of("교대역")).as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음(StationRequest.of("남부터미널역")).as(StationResponse.class);

        LineRequest 신분당선_Request = LineRequest.of("신분당선", "RED", 강남역.getId(), 양재역.getId(), DISTANCE_10);
        LineRequest 이호선_Request = LineRequest.of("이호선", "GREED", 교대역.getId(), 강남역.getId(), DISTANCE_10);
        LineRequest 삼호선_Request = LineRequest.of("삼호선", "ORANGE", 남부터미널역.getId(), 양재역.getId(), DISTANCE_5);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_Request).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_Request).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_Request).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), SectionRequest.of(교대역.getId(), 남부터미널역.getId(), DISTANCE_3));
    }

    @DisplayName("출발역에서 도착역까지 최단경로를 조회한다.")
    @Test
    void findShortestPath1() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_조회됨(response,
                           Arrays.asList(교대역, 남부터미널역, 양재역),
                           DISTANCE_3 + DISTANCE_5,
                           BASIC.getFare());
    }

    @DisplayName("출발역과 도착역이 같을 때, 최단경로를 조회한다.")
    @Test
    void findShortestPath2() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        지하철_최단경로_조회됨(response, Collections.singletonList(교대역), DISTANCE_0, 0);
    }

    @DisplayName("출발역이 노선상에 존재하지 않을때, 최단경로를 조회한다.")
    @Test
    void findShortestPath3() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(서울역.getId(), 교대역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("도착역이 노선상에 존재하지 않을때, 최단경로를 조회한다.")
    @Test
    void findShortestPath4() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 서울역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("출발역과 도착역이 서로 연결되어 있지 않을때, 최단경로를 조회한다.")
    @Test
    void findShortPath5() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);
        StationResponse 시청역 = 지하철역_등록되어_있음(StationRequest.of("시청역")).as(StationResponse.class);

        LineRequest 일호선_Request = LineRequest.of("일호선", "BLUE", 서울역.getId(), 시청역.getId(), DISTANCE_5);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(일호선_Request).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 서울역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }
}
