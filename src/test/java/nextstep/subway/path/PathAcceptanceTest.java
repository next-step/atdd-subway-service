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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 동작역;

    /**
     * 교대역   --- *2호선* 10km ---   강남역
     * |                             |
     * *3호선* 3km                *신분당선* 10km
     * |                             |
     * 남부터미널역 --- *3호선* 2km ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        동작역 = StationAcceptanceTest.지하철역_등록되어_있음("동작역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 300);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 사당역, 동작역, 4, 400);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Feature: 지하철 경로 검색
     * <p>
     * Scenario: 두 역의 최단 거리 경로를 조회
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리도 함께 응답
     * And 지하철 이용 요금도 함께 응답
     */
    @DisplayName("최단 경로를 조회한다.")
    @Test
    void 경로_조회_1() {
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(강남역, 남부터미널역);

        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(12);
        assertThat(path.getFare()).isEqualTo(1350);
    }

    /**
     * Given 노선 간에 접점이 생기도록 역을 추가했을 때
     * When 출발역부터 도착역까지의 최단 경로를 조회하면
     * Then 최단 경로의 지하철역들과 총 거리를 응답받을 수 있다
     */
    @DisplayName("노선이 만나는 역이 추가되었을 때 최단 경로를 조회한다.")
    @Test
    void 경로_조회_2() {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(사호선, 동작역, 양재역, 3);

        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(강남역, 남부터미널역);

        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(12);
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare)).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }
}
