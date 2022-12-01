package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.path.dto.PathsResponse;
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
                        5))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회")
    void getPaths() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역, 양재역);

        // then
        지하철_최단_경로_응답됨(response);
        지하철_최단_경로_결과_비교(response, Arrays.asList(남부터미널역.getId()));
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 - 출발역과 도착역이 같은 경우")
    void getPathsSameSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역, 양재역);

        // then
        지하철_최단_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
    void getPathsNotLinkedSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역, 양재역);

        // then
        지하철_최단_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void getPathsNotFoundSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역, 양재역);

        // then
        지하철_최단_경로_응답_실패됨(response);
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source=" + source.getId() + "&target=" + target.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_최단_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathsResponse.class)).isNotNull();
    }

    public static void 지하철_최단_경로_결과_비교(ExtractableResponse<Response> response, List<Long> ids) {
        PathsResponse pathsResponse = response.as(PathsResponse.class);
        List<Long> pathStationIds = pathsResponse.getStations().stream()
                .map(PathStation::getId)
                .collect(Collectors.toList());
        assertThat(pathStationIds.containsAll(ids)).isTrue();
    }

    public static void 지하철_최단_경로_응답_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}
