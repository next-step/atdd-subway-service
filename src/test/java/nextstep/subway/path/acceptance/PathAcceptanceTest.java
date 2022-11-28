package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTestFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestFixture.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestFixture.지하철_최단경로_조회_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestFixture.지하철_최단경로_조회_요청_실패됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestFixture.지하철_최단경로_조회_요청_응답됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestFixture.지하철_최단경로_조회_요청_포함됨;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 마곡역;
    private StationResponse 마포역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * 마곡역 --- *5호선* --- 마포역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        마곡역 = 지하철역_등록되어_있음("마곡역").as(StationResponse.class);
        마포역 = 지하철역_등록되어_있음("마포역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        오호선 = 지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 마곡역.getId(), 마포역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철 역과 역 사이의 최단 거리를 조회한다.")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(강남역, 남부터미널역);

        // then
        지하철_최단경로_조회_요청_응답됨(response);
        지하철_최단경로_조회_요청_포함됨(response, Arrays.asList("강남역", "양재역", "남부터미널역"));
    }

    @DisplayName("출발역과 도착역이 동일한 경우 에러가 발생된다.")
    @Test
    void validateSameStationException() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(강남역, 강남역);

        // then
        지하철_최단경로_조회_요청_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러가 발생된다.")
    @Test
    void validateNotConnectException() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(강남역, 마곡역);

        // then
        지하철_최단경로_조회_요청_실패됨(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우 에러가 발생된다.")
    @Test
    void validateNotExistStationException() {
        // given
        StationResponse 김포공항역 = 지하철역_등록되어_있음("김포공항역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(강남역, 김포공항역);

        // then
        지하철_최단경로_조회_요청_실패됨(response);
    }
}
