package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("경로를 검색하면 최단거리를 반환한다")
    @Test
    void testGetShortCut() {
        // when
        ExtractableResponse<Response> response = 역_사이의_최단경로_요청(교대역, 양재역);

        // then
        최단_경로_응답됨(response);
        최단_거리_경로를_응답(response, "교대역", "남부터미널역", "양재역");
        총_거리도_함께_응답(response, 5);
        지하철_이용_요금도_함께_응답함(response, 1250);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void testNotLinked() {
        // given
        StationResponse 삼봉역 = StationAcceptanceTest.지하철역_등록되어_있음("삼봉역").as(StationResponse.class);
        StationResponse 박달사거리역 = StationAcceptanceTest.지하철역_등록되어_있음("박달사거리역").as(StationResponse.class);
        LineAcceptanceTest.지하철_노선_등록되어_있음("박달선", "bg-red-600", 삼봉역, 박달사거리역, 5);

        // when
        ExtractableResponse<Response> response = 역_사이의_최단경로_요청(강남역, 박달사거리역);

        // then
        최단_경로_찾기_실패_응답함(response);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void testSameStation() {
        // when
        ExtractableResponse<Response> response = 역_사이의_최단경로_요청(강남역, 강남역);

        // then
        최단_경로_찾기_실패_응답함(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void testHasNotStation() {
        // when
        ExtractableResponse<Response> response = 역_사이의_최단경로_요청(999999L, 강남역.getId());

        // then
        역_찾을_수_없음_응답(response);
    }

    private void 역_찾을_수_없음_응답(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private void 최단_경로_찾기_실패_응답함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 최단_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_거리_경로를_응답(ExtractableResponse<Response> response, String ... stationNames) {
        PathResponse pathResponse = response.jsonPath().getObject("", PathResponse.class);
        assertThat(pathResponse.getStations())
                .map(StationResponse::getName)
                .containsExactly(stationNames);
    }

    private void 총_거리도_함께_응답(ExtractableResponse<Response> response, int distance) {
        PathResponse pathResponse = response.jsonPath().getObject("", PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private void 지하철_이용_요금도_함께_응답함(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.jsonPath().getObject("", PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    private ExtractableResponse<Response> 역_사이의_최단경로_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source, target)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 역_사이의_최단경로_요청(StationResponse source, StationResponse target) {
        return 역_사이의_최단경로_요청(source.getId(), target.getId());
    }
}
