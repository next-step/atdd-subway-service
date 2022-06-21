package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
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
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
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

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 3000)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 3000)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("3호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 4500)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 2000);
    }

    @Test
    @DisplayName("최단경로 조회 시 출발역부터 도착지까지의 역을 순서대로 출력한다.")
    void findShortestPath() {
        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(양재역.getId(), 교대역.getId());

        // then
        최단경로_조회됨(최단경로, Arrays.asList(양재역, 남부터미널역, 교대역));
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 BAD REQUEST를 리턴한다.")
    void returnBadRequestWhenSameSourceTargetStation() {
        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(양재역.getId(), 양재역.getId());

        // then
        같은_역_최단경로_조회_실패됨(최단경로);
    }

    @Test
    @DisplayName("출발역이나 도착역이 존재하지 않는 경우 NOT FOUND를 리턴한다.")
    void returnNotFoundWhenNotExistingStation() {
        // given
        StationResponse 미등록역 = StationResponse.of(new Station("미등록역"));

        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(미등록역.getId(), 양재역.getId());

        // then
        존재하지_않는_역_최단경로_조회_실패됨(최단경로);
    }


    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않는 경우 BAD REQUEST를 리턴한다.")
    void returnBadRequestWhenNotConnected() {
        // given
        StationResponse 정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(정자역.getId(), 양재역.getId());

        // then
        연결되지_않은_역_최단경로_조회_실패됨(최단경로);
    }

    public static ExtractableResponse<Response> 최단경로_조회_요청(Long sourceStationId, Long targetStationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(PathRequest.of(sourceStationId, targetStationId))
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static void 최단경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStations) {
        List<StationResponse> stations = response.as(PathResponse.class).getStations();
        List<Long> actualIds = stations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedIds = expectStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds)
                .containsExactlyElementsOf(expectedIds);
    }

    private static void 같은_역_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 존재하지_않는_역_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private static void 연결되지_않은_역_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
