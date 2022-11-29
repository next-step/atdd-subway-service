package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 인천역;
    private StationResponse 부평역;

    /**
     * 교대역    --- *2호선(10)* ---   강남역
     * |                              |
     * *3호선(3)*                   *신분당선(10)*
     * |                              |
     * 남부터미널역  --- *3호선(2)* --- 양재
     *
     * 인천 --1호선 (5)-- 부평 (연결되어 있지 않은 노선)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        인천역 = 지하철역_등록되어_있음("인천역").as(StationResponse.class);
        부평역 = 지하철역_등록되어_있음("부평역").as(StationResponse.class);

        지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 0);
        지하철_노선_등록되어_있음("이호선", "bg-blue-600", 교대역.getId(), 강남역.getId(), 10, 0);
        지하철_노선_등록되어_있음("일호선", "bg-yellow-600", 인천역.getId(), 부평역.getId(), 5, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-400", 교대역.getId(), 양재역.getId(), 5, 0);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철 역 사이의 최단 경로를 조회한다")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(강남역, 남부터미널역);

        // then
        최단경로_요청이_정상_조회됨(response, 12, 1_350, 강남역, 양재역, 남부터미널역);
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 같다면 예외가 발생한다")
    @Test
    void sameStationException() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역, 교대역);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 연결이 되어 있지 않다면 예외가 발생한다")
    @Test
    void notConnectException() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(양재역, 인천역);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로를 조회 시, 존재하지 않은 출발역이나 도착역을 조회 할 경우 예외가 발생한다")
    @Test
    void notExistException() {
        // given
        StationResponse 존재하지_않는_역 = 지하철역_등록되어_있음("미궁역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역, 존재하지_않는_역);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로 조회 시, 지하철 이용요금도 조회된다.")
    @Test
    void extraFare() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(강남역, 남부터미널역);

        // then
        최단경로_요청이_정상_조회됨(response, 12, 1_350, 강남역, 양재역, 남부터미널역);
    }

    private void 최단경로_요청이_정상_조회됨(
            ExtractableResponse<Response> response,
            int distance,
            int fare,
            StationResponse... stations
    ) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> actualIds = Arrays.asList(stations)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedIds = pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());


        assertThat(actualIds).containsAll(expectedIds);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    private static ExtractableResponse<Response> 최단경로_조회_요청(
            StationResponse upStation,
            StationResponse downStation
    ) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={upStationId}&target={downStationId}", upStation.getId(), downStation.getId())
                .then().log().all()
                .extract();
    }

    private static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
