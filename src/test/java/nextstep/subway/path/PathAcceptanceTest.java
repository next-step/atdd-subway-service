package nextstep.subway.path;

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
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 신도림역;
    private StationResponse 영등포역;

    /**
     * 신도림역  --- *1호선* ---- 영등포역
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        신도림역 = 지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        영등포역 = 지하철역_등록되어_있음("영등포역").as(StationResponse.class);

        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 400));
        지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-blue-600", 신도림역.getId(), 영등포역.getId(), 20, 0));
        지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 300));
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 200))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("출발역부터 도착역까지 최단경로, 총 거리, 이용 요금 조회한다.")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());
        PathResponse pathResponse = response.as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getStations().get(0)).isEqualTo(교대역),
                () -> assertThat(pathResponse.getStations().get(1)).isEqualTo(남부터미널역),
                () -> assertThat(pathResponse.getStations().get(2)).isEqualTo(양재역),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 조회하지 못한다.")
    @Test
    void findShortestPathException1() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(양재역.getId(), 양재역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우, 조회하지 못한다.")
    @Test
    void findShortestPathException2() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(신도림역.getId(), 양재역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우, 조회하지 못한다.")
    @Test
    void findShortestPathException3() {
        StationResponse 노량진역 = 지하철역_등록되어_있음("노량진역").as(StationResponse.class);

        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 노량진역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }
}
