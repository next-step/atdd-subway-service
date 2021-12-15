package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
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
    private StationResponse 정자역;

    /**
     * 교대역  --- *2호선(10)* --- 강남역
     * |                           |
     * *4호선(3)*                  *신분당선(10)*
     * |                           |
     * 남부터미널역 --- *3호선(2)* --- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        RestAssured.defaultParser = Parser.JSON;

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private static LineResponse 지하철_노선_등록되어_있음(String name, String color,
        StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest param = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(param).as(LineResponse.class);
    }

    private static void 지하철_노선에_지하철역_등록되어_있음(LineResponse line,
        StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(Long srcStationId, Long destStationId) {
        PathRequest param = new PathRequest(srcStationId, destStationId);
        return 지하철_경로_조회_요청(param);
    }
    public static ExtractableResponse<Response> 지하철_경로_조회_요청(PathRequest params) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams("source", params.getSrcStationId())
            .queryParams("target", params.getDestStationId())
            .when().get("/path")
            .then().log().all().
                extract();
    }

    private void 최단_경로_조회됨(ExtractableResponse<Response> response, StationResponse... expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPath())
            .containsExactly(expectedStations);
    }

    private void 최단_경로_거리_일치함(ExtractableResponse<Response> response, double expectedWeights) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPathWeight()).isEqualTo(expectedWeights);
    }

    private void 최단_경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("출발역에서 도착역으로 가는 최단 경로를 조회한다")
    @Test
    public void findShortestPathTest() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        최단_경로_조회됨(response, 교대역, 남부터미널역, 양재역);
        최단_경로_거리_일치함(response, 5);
    }

    @DisplayName("출발역에서 도착역이 동일할 때 조회 실패한다.")
    @Test
    public void findShortestPathTest_sameSrcAndDest() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        최단_경로_조회_실패함(response);
    }

    @DisplayName("출발역이 대상 노선 내에 없을 때 조회 실패한다.")
    @Test
    public void findShortestPathTest_stationNotExist() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(정자역.getId(), 양재역.getId());

        // then
        최단_경로_조회_실패함(response);
    }
}