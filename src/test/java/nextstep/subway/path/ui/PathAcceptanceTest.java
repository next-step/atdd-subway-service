package nextstep.subway.path.ui;

import io.restassured.RestAssured;
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
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 구리역;
    private StationResponse 도농역;
    private LineResponse 중앙선;
    private StationResponse 용산역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        구리역 = StationAcceptanceTest.지하철역_등록되어_있음("구리역").as(StationResponse.class);
        도농역 = StationAcceptanceTest.지하철역_등록되어_있음("도농역").as(StationResponse.class);
        용산역 = StationAcceptanceTest.지하철역_등록되어_있음("용산역").as(StationResponse.class);
        중앙선 = 지하철_노선_등록되어_있음(new LineRequest("중앙선", "bg-red-300", 구리역.getId(), 도농역.getId(), 100)).as(LineResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-300", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-400", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-500", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3));
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void getShortestPath() {
        최단_경로가_조회됨(최단_경로를_조회(강남역.getId(), 남부터미널역.getId()), 12);
    }

    private ExtractableResponse<Response> 최단_경로를_조회(long source, long target) {
        return RestAssured
                .given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static void 최단_경로가_조회됨(ExtractableResponse response, int distance) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void fail_test1() {
        최단_경로가_조회되지_않음(최단_경로를_조회(강남역.getId(), 강남역.getId()));
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void fail_test2() {
        최단_경로가_조회되지_않음(최단_경로를_조회(강남역.getId(), 구리역.getId()));
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void fail_test3() {
        최단_경로가_조회되지_않음(최단_경로를_조회(강남역.getId(), 용산역.getId()));
    }

    private void 최단_경로가_조회되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}