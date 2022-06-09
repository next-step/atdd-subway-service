package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTestMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestMethod.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_요금도_함께_조회됨;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_조회_실패;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_조회_요청;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_조회됨;
import static nextstep.subway.station.StationAcceptanceTest.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private static final int BASIC_FARE = 1250;
    private static final int TRD_LINE_FARE = 300;
    private static final int SEC_LINE_FARE = 200;
    private static final int NBD_LINE_FARE = 500;

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

        LineRequest 신분당선_Request = LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, NBD_LINE_FARE);
        LineRequest 이호선_Request = LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, SEC_LINE_FARE);
        LineRequest 삼호선_Request = LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 5, TRD_LINE_FARE);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_Request).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_Request).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_Request).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), SectionRequest.of(교대역.getId(), 남부터미널역.getId(), 3));
    }

    @DisplayName("출발역에서 도착역까지 최단경로를 조회한다.")
    @Test
    void findShortPath01() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역), 8);
    }

    @DisplayName("출발역과 도착역이 같은 경우, 최단경로 조회가 실패한다.")
    @Test
    void exceptionFindShortPath01() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        지하철_최단경로_조회됨(response, Arrays.asList(교대역), 0);
    }

    @DisplayName("존재하지 않은 출발역을 조회하는 경우, 최단경로 조회가 실패한다.")
    @Test
    void exceptionFindShortPath02() {
        // given
        StationResponse 수서역 = 지하철역_등록되어_있음("수서역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(수서역.getId(), 교대역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("존재하지 않은 도착역을 조회하는 경우, 최단경로 조회가 실패한다.")
    @Test
    void exceptionFindShortPath03() {
        // given
        StationResponse 수서역 = 지하철역_등록되어_있음("수서역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 수서역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }

    /**
     * given. 지하철 역이 등록되어 있음.
     * and. 지하철 노선이 등록되어 있음.
     * and. 지하철 노선에 지하철역이 등록되어 있음.
     * when. 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * then. 최단 거리 경로를 응답
     * and. 총 거리도 함께 응답함.
     * and. ** 지하철 이용 요금도 함께 응답함 **
     */
    @DisplayName("두 역의 최단 거리 경로를 조회할 수 있다.")
    @Test
    void findShortPath02() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_요금도_함께_조회됨(response,
                Arrays.asList(교대역, 남부터미널역, 양재역),
                8,
                BASIC_FARE + TRD_LINE_FARE);
    }
}
