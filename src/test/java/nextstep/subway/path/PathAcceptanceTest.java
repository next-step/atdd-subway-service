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

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * When 교대역에서 양재역까지 최단경로를 조회요청
     * Then 교대역-남부터미널역-양재역 경로가 조회됨
     * Then 최단거리는 5로 조회됨
     */
    @Test
    void 교대역에서_양재역까지_최단경로() {
        // when
        ExtractableResponse<Response> 조회_응답 = 최단경로_조회_요청(교대역, 양재역);

        // then
        최단경로_응답됨(조회_응답);
        최단경로_지하철역_순서_정렬됨(조회_응답, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단경로_거리_일치함(조회_응답, 5);
    }

    /**
     * When 강남역에서 남부터미널역까지 최단경로를 조회요청
     * Then 강남역-양재역-남부터미널역 경로가 조회됨
     * Then 최단거리는 12로 조회됨
     */
    @Test
    void 강남역에서_남부터미널역까지_최단경로() {
        // when
        ExtractableResponse<Response> 조회_응답 = 최단경로_조회_요청(강남역, 남부터미널역);

        // then
        최단경로_응답됨(조회_응답);
        최단경로_지하철역_순서_정렬됨(조회_응답, Arrays.asList(강남역, 양재역, 남부터미널역));
        최단경로_거리_일치함(조회_응답, 12);
    }

    /**
     * When 출발역과 도착역이 같은경우 최단경로를 조회요청하면
     * Then 실패한다
     */
    @Test
    void 출발역과_도착역이_같은경우() {
        // when
        ExtractableResponse<Response> 조회_응답 = 최단경로_조회_요청(강남역, 강남역);

        // then
        최단경로_응답_실패(조회_응답);
    }

    public static void 최단경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단경로_응답_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 최단경로_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 최단경로_거리_일치함(ExtractableResponse<Response> response, int expected) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getDistance()).isEqualTo(expected);
    }

    public static ExtractableResponse<Response> 최단경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStation.getId())
                .param("target", targetStation.getId())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
