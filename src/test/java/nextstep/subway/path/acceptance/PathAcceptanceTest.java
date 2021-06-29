package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
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
import org.springframework.http.MediaType;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
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
    private StationResponse 역삼역;
    private StationResponse 남부터미널역;
    private StationResponse 공덕역;

    /**
     * 교대역    --- *2호선* ---   강남역   -- 역삼역
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
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        공덕역 = 지하철역_등록되어_있음("공덕역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 3);
    }

    @Test
    @DisplayName("역과 역 사이의 최단 경로를 조회한다")
    void findPathTest() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(남부터미널역.getId(), 역삼역.getId());

        // then
        최단경로_조회됨(response);
        assertThat(response.jsonPath().getObject(".", PathResponse.class).getStations()).hasSize(4);
        assertThat(response.jsonPath().getObject(".", PathResponse.class).getDistance()).isEqualTo(15);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다")
    void sameSourceTargetTest() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(역삼역.getId(), 역삼역.getId());

        // then
        최단경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외가 발생한다")
    void notConnectedSourceTargetTest() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(역삼역.getId(), 공덕역.getId());

        // then
        최단경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않는 출발역이나 도착역인 경우 예외가 발생한다")
    void notExistStationTest() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(역삼역.getId(), 10L);

        // then
        최단경로_조회_실패됨(response);
    }

    private void 최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", sourceId, targetId)
                .then().log().all().extract();
    }
}
