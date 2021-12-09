package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_구간_등록_요청;
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

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 500)
        ).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)
        ).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 200)
        ).as(LineResponse.class);

        지하철_구간_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("존재하지 않은 출발역이나 도착열을 조회 할 경우")
    @Test
    void notFoundStationException() {
        // when
        ExtractableResponse<Response> response = 경로_조회를_요청함(10L, 교대역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("구간에 지하철역이 존재하지 않을 경우")
    @Test
    void notFoundStationInSections() {
        // given
        StationResponse 잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 경로_조회를_요청함(교대역.getId(), 잠실역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void notLinkedStations() {
        // given
        StationResponse 잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        StationResponse 천호역 = 지하철역_등록되어_있음("천호역").as(StationResponse.class);
        지하철_구간_등록_요청(이호선, 잠실역, 천호역, 10);

        // when
        ExtractableResponse<Response> response = 경로_조회를_요청함(강남역.getId(), 천호역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void equalsStations() {
        // when
        ExtractableResponse<Response> response = 경로_조회를_요청함(강남역.getId(), 강남역.getId());

        // then
        경로_조회_실패됨(response);
    }

    private void 경로_조회_실패됨(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("경로를 조회함")
    @Test
    void pathTest() {
        // when
        ExtractableResponse<Response> response = 경로_조회를_요청함(강남역.getId(), 남부터미널역.getId());

        // then
        경로_조회됨(response, Arrays.asList(강남역, 양재역, 남부터미널역), 12, 1850);
    }

    private void 경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> stations, int distance, int fare) {
        요청_결과_검증(response, HttpStatus.OK);
        PathResponse paths = response.as(PathResponse.class);
        assertThat(paths.getStations()).isEqualTo(stations);
        assertThat(paths.getDistance()).isEqualTo(distance);
        assertThat(paths.getFare()).isEqualTo(fare);
    }

    private ExtractableResponse<Response> 경로_조회를_요청함(Long sourceId, Long targetId) {
        PathRequest pathRequest = new PathRequest(sourceId, targetId);

        return RestAssured
                .given().log().all()
                .body(pathRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }
}

