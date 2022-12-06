package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 별도호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 이어지지않은역_1;
    private StationResponse 이어지지않은역_2;
    private StationResponse 없는역;

    /**
     * 교대역  ---  *2호선* 10 ---   강남역
     * |                         |
     * *3호선*  3                 *신분당선* 10
     * |                         |
     * 남부터미널역--- *3호선* 12 ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(nextstep.subway.station.dto.StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(nextstep.subway.station.dto.StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(nextstep.subway.station.dto.StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(nextstep.subway.station.dto.StationResponse.class);

        이어지지않은역_1 = StationAcceptanceTest.지하철역_등록되어_있음("이어지지않은역_1").as(nextstep.subway.station.dto.StationResponse.class);
        이어지지않은역_2 = StationAcceptanceTest.지하철역_등록되어_있음("이어지지않은역_2").as(nextstep.subway.station.dto.StationResponse.class);

        없는역 = new nextstep.subway.station.dto.StationResponse(999L, "없는역", null, null);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(
                        "신분당선",
                        "bg-red-600",
                        강남역.getId(),
                        양재역.getId(),
                        10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(
                        "이호선",
                        "bg-red-600",
                        교대역.getId(),
                        강남역.getId(),
                        10))
                .as(LineResponse.class);

        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(
                        "삼호선",
                        "bg-red-600",
                        교대역.getId(),
                        양재역.getId(),
                        15))
                .as(LineResponse.class);

        별도호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(
                        "별도호선",
                        "bg-red-600",
                        이어지지않은역_1.getId(),
                        이어지지않은역_2.getId(),
                        5))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회")
    void getPaths() {
        // when
        ExtractableResponse<Response> response_distance_15 = 지하철_최단_경로_조회_요청(교대역, 양재역);
        ExtractableResponse<Response> response_distance_3 = 지하철_최단_경로_조회_요청(교대역, 남부터미널역);

        // then
        지하철_최단_경로_응답됨(response_distance_15);
        지하철_최단_경로_결과_비교(response_distance_15, Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()), 15, 1350);
        지하철_최단_경로_응답됨(response_distance_3);
        지하철_최단_경로_결과_비교(response_distance_3, Arrays.asList(교대역.getId(), 남부터미널역.getId()), 3, 1250);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 - 출발역과 도착역이 같은 경우")
    void getPathsSameSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역, 교대역);

        // then
        지하철_최단_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
    void getPathsNotLinkedSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역, 이어지지않은역_1);

        // then
        지하철_최단_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void getPathsNotFoundSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(없는역, 양재역);

        // then
        지하철_최단_경로_응답_실패됨(response);
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(nextstep.subway.station.dto.StationResponse source, nextstep.subway.station.dto.StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source=" + source.getId() + "&target=" + target.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_최단_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class)).isNotNull();
    }

    public static void 지하철_최단_경로_결과_비교(ExtractableResponse<Response> response, List<Long> ids, int distance, int price) {
        PathResponse pathResponse = response.as(PathResponse.class);
        최단_경로_역_포함_비교(pathResponse, ids);
        최단_경로_거리_비교(pathResponse, distance);
        최단_경로_금액_비교(pathResponse, price);
    }

    private static void 최단_경로_역_포함_비교(PathResponse pathResponse, List<Long> ids) {
        List<Long> pathStationIds = pathResponse.getStations().stream()
                .map(PathStationResponse::getId)
                .collect(Collectors.toList());
        assertThat(pathStationIds.containsAll(ids)).isTrue();
    }

    private static void 최단_경로_거리_비교(PathResponse pathResponse, int distance) {
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private static void 최단_경로_금액_비교(PathResponse pathResponse, int price) {
        assertThat(pathResponse.getPrice()).isEqualTo(price);
    }

    public static void 지하철_최단_경로_응답_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}
