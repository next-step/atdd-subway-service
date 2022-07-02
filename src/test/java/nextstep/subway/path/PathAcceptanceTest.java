package nextstep.subway.path;


import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 부산역;

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
        부산역 = StationAcceptanceTest.지하철역_등록되어_있음("부산역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * when 등록된 역과 역 사이에 최단 경로 조회 요청을 한다.
     * then 역 사이의 최단 경로를 순서대로 확인한다.
     */
    @DisplayName("역과 역 사이에서 가장 짧은 경로를 찾아서 역 순서대로 조회한다.")
    @Test
    public void findShortestPath() {
        //when
        ExtractableResponse<Response> 최단경로_조회응답 = 지하철_역사이_최단경로_조회요청(강남역, 남부터미널역);
        //then
        지하철_노선에_지하철역_순서_정렬됨(최단경로_조회응답, Arrays.asList(강남역, 양재역, 남부터미널역));
        지하철_역사이_최단경로_거리확인(최단경로_조회응답, 12);
        최단경로_요금조회(최단경로_조회응답, Fare.of(1350));
    }

    /**
     * when 출발역과 도착역을 동일하게 하여 최단 경로 조회를 요청한다.
     * then 조회 요청이 실패한다.
     */
    @DisplayName("출발역과 도착역이 같은 경우 최단 경로 조회를 실패한다.")
    @Test
    public void findShortestPathWithSameStation() {
        //when
        ExtractableResponse<Response> 최단경로_조회응답 = 지하철_역사이_최단경로_조회요청(강남역, 강남역);
        //then
        지하철_역사이_최단경로_조회_실패함(최단경로_조회응답);
    }

    /**
     * when 출발역과 도착역이 연결되어 있지 않은 상태로 최단 경로 조회를 요청한다.
     * then 조회 요청이 실패한다.
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 최단 경로 조회를 실패한다.")
    @Test
    public void findShortestPathWithNotConnectedStation() {
        //when
        ExtractableResponse<Response> 최단경로_조회응답 = 지하철_역사이_최단경로_조회요청(강남역, 부산역);
        //then
        지하철_역사이_최단경로_조회_실패함(최단경로_조회응답);
    }

    /**
     * when 존재하지 않은 역으로 최단 경로 조회를 요청한다.
     * then 조회 요청이 실패한다.
     */
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 최단 경로 조회를 실패한다.")
    @Test
    public void findShortestPathWithNotExistStation() {
        //when
        ExtractableResponse<Response> 최단경로_조회응답 = 지하철_역사이_최단경로_조회요청(new StationResponse(9999l, "존재하지 않는 역"), 강남역);
        //then
        지하철_역사이_최단경로_조회_실패함(최단경로_조회응답);
    }


    public static ExtractableResponse<Response> 지하철_역사이_최단경로_조회요청(StationResponse sourceStation, StationResponse targetStation) {

        return RestAssured
            .given().log().all()
            .param("source",sourceStation.getId())
            .param("target",targetStation.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("paths")
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }


    public static void 지하철_역사이_최단경로_거리확인(ExtractableResponse<Response> response, long distance) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    public static void 최단경로_요금조회(ExtractableResponse<Response> response, Fare fare) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getFare()).isEqualTo(fare.value());
    }


    public static void 지하철_역사이_최단경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
