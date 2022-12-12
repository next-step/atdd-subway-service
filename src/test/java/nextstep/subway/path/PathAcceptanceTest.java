package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineRestAssured;
import nextstep.subway.line.acceptance.LineSectionRestAssured;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.path.PathRestAssured.지하철_최단_경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Feature 지하철 경로 관련 기능
 * Background
 * Given 지하철역(Station)이 여러개 등록되어 있음
 * And 지하철노선(Line) 여러개 등록되어 있음
 * And 지하철노선에 지하철역(Section) 여러개 등록되어 있음
 * <p>
 * Scenario 출발역과 도착역 사이의 최단 경로 조회
 * When 지하철 최단 경로 조회 요청
 * Then 출발역과 도착역 사이의 최단 경로 조회됨
 * <p>
 * Scenario 출발역과 도착역이 같을 때 최단 경로 조회
 * When 지하철 최단 경로 조회 요청
 * Then 최단 경로 조회 실패
 * <p>
 * Scenario 존재하지 않은 출발역 또는 도착역으로 최단 경로 조회
 * When 지하철 최단 경로 조회 요청
 * Then 최단 경로 조회 실패
 */
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 이수역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * <p>
     * 사당역----4호선(5)------이수역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        이수역 = StationAcceptanceTest.지하철역_등록되어_있음("이수역").as(StationResponse.class);

        신분당선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        사호선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-blue-600", 사당역.getId(), 이수역.getId(), 5)).as(LineResponse.class);

        LineSectionRestAssured.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로 조회")
    @Test
    void findTheShortestPath() {
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역.getId(), 강남역.getId());
        지하철_최단_경로_조회_조회됨(response,10);
    }

    @DisplayName("예외발생 - 출발역과 도착역이 같은데 조회하는 경우")
    @Test
    void makeExceptionWhenSourceStationAndTargetStationIsEqual() {
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역.getId(), 교대역.getId());
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("예외발생 - 연결되어 있지 않은 출발역과 도착역으로 최단 경로 조회")
    @Test
    void makeExceptionWhenSourceStationAndTargetStationIsNotBelongToSameLine() {
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역.getId(), 사당역.getId());
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("예외발생 - 존재하지 않은 출발역으로 최단 경로 조회")
    @Test
    void makeExceptionWhenSourceStationIsNotExist() {
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역.getId(), 10L);
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("예외발생 - 존재하지 않은 도착역으로 최단 경로 조회")
    @Test
    void makeExceptionWhenTargetStationIsNotExist() {
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(10L, 교대역.getId());
        지하철_최단_경로_조회_실패됨(response);
    }

    public static void 지하철_최단_경로_조회_조회됨(ExtractableResponse<Response> response, int expectDistance) {
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(expectDistance);
        });
    }

    public static void 지하철_최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
