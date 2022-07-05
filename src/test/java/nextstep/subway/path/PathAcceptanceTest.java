package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.AbstractDoubleAssert;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("jgrapht문법 테스트")
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("역과 역사이의 최단거리 조회 테스트")
    void findShortDistance() {
        //when
        ExtractableResponse<Response> response = 출발역_도착역_최단거리_조회(교대역.getId(), 양재역.getId());

        //then
        출발역_도착역_최단거리_비교하기_역정보(response.as(PathResponse.class).getStations(), Arrays.asList(교대역, 남부터미널역, 양재역));
    }

    @Test
    @DisplayName("경로를 검색하고 가격을 비교하는 테스트")
    void findShortPrice() {
        //when
        ExtractableResponse<Response> response = 출발역_도착역_최단거리_조회(교대역.getId(), 양재역.getId());

        //then
        assertAll(
                () -> 출발역_도착역_최단거리_비교하기_거리(response, 5),
                () -> 출발역_도착역_최단거리_비교하기_역정보(response.as(PathResponse.class).getStations(), Arrays.asList(교대역, 남부터미널역, 양재역)),
                () -> 출발역_도착역_최단거리_비교하기_금액(response, 1250)
        );
    }

    private AbstractDoubleAssert<?> 출발역_도착역_최단거리_비교하기_금액(ExtractableResponse<Response> response, int price) {
        return assertThat(response.as(PathResponse.class).getPrice()).isEqualTo(price);
    }

    private AbstractDoubleAssert<?> 출발역_도착역_최단거리_비교하기_거리(ExtractableResponse<Response> response, int distance) {
        return assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    private void 출발역_도착역_최단거리_비교하기_역정보(List<StationResponse> stations, List<StationResponse> expectedStations) {
        List<Long> stationIds = stations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    @Test
    @DisplayName("존재하지 않는 역에 대한 경로를 검색하려는 경우 실패하는 테스트")
    void findShortDistanceFail1() {
        //when
        ExtractableResponse<Response> response = 출발역_도착역_최단거리_조회(교대역.getId(), 1234L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("같은 역에 대한 경로를 검색하려는 경우 실패하는 테스트")
    void findShortDistanceFail2() {
        //when
        ExtractableResponse<Response> response = 출발역_도착역_최단거리_조회(교대역.getId(), 교대역.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("경로가 존재하지 않는 경우 실패하는 테스트")
    void findShortDistanceFail3() {
        //given
        StationResponse 신도림역 = StationAcceptanceTest.지하철역_등록되어_있음("신도림역").as(StationResponse.class);

        //when
        ExtractableResponse<Response> response = 출발역_도착역_최단거리_조회(교대역.getId(), 신도림역.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선_생성_요청(name, color, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        //given
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse, upStation, downStation, distance);

        //when
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(lineResponse);

        //then
        지하철_노선에_지하철역_확인(response, Arrays.asList(upStation, downStation));
    }

    public static void 지하철_노선에_지하철역_확인(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsAll(expectedStationIds);
    }

    public static ExtractableResponse<Response> 출발역_도착역_최단거리_조회(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={source}&target={target}", source, target)
                .then().log().all().
                extract();
    }

    public static void 최단거리(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsAll(expectedStationIds);
    }

}
