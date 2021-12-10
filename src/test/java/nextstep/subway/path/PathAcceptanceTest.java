package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 구호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 당산역;
    private StationResponse 선유도역;

    @Autowired
    private LineService lineService;

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
        당산역 = StationAcceptanceTest.지하철역_등록되어_있음("당산역").as(StationResponse.class);
        선유도역 = StationAcceptanceTest.지하철역_등록되어_있음("선유도역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-red-600", 당산역, 선유도역, 20);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("최단거리 테스트")
    void shortestPathTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 양재역.getId());
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getDistance()).isEqualTo(5);
        List<String> stationNames = pathResponse.getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());
        assertThat(stationNames).contains("교대역", "남부터미널역", "양재역");

    }

    @Test
    @DisplayName("시작역과 종료역이 같을 때 에러처리")
    void isSameSourceAndTargetTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 교대역.getId());
        지하철_탐색_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 source 역 탐색할때 에러처리")
    void isNotExistSourceTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(-1L, 교대역.getId());
        지하철_탐색_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 target 역 탐색할때 에러처리")
    void isNotExistTargetTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), -1L);
        지하철_탐색_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어있지 않은 경우 에러처리")
    void isSourceAndTargetStationsNotConnectLineTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 당산역.getId());
        지하철_탐색_실패됨(response);
    }

    public static ExtractableResponse<Response> 최단_거리_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 지하철_탐색_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        lineService.addLineStation(lineResponse.getId(), sectionRequest);
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance);
        return lineService.saveLine(lineRequest);
    }

}
