package nextstep.subway.path.acceptance;

import com.google.common.collect.ImmutableList;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 부평역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");
        남부터미널역 = createStation("부평역");

        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 14);
        이호선 = createLine("이호선", "bg-green-600", 교대역, 강남역, 12);
        삼호선 = createLine("삼호선", "bg-orange-600", 교대역, 남부터미널역, 9);

        createSection(삼호선, 남부터미널역, 양재역, 18);
    }

    private StationResponse createStation(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    private LineResponse createLine(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance))
                                 .as(LineResponse.class);
    }

    private void createSection(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    @DisplayName("출발역과 도착역이 같으면 오류")
    @Test
    void findPathFail01() {
        findPathFail(강남역, 강남역);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 오류")
    @Test
    void findPathFail02() {
        findPathFail(강남역, 부평역);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회하면 오류")
    @Test
    void findPathFail03() {

        LocalDateTime dontCareTime = LocalDateTime.now();
        StationResponse noExistStation1 = new StationResponse(10000L, "NO_1", dontCareTime, dontCareTime);
        StationResponse noExistStation2 = new StationResponse(20000L, "NO_2", dontCareTime, dontCareTime);

        findPathFail(noExistStation1, noExistStation2);
    }

    @DisplayName("최단 거리 탐색 - 같은 노선")
    @Test
    void findPathSuccess01() {
        findPathSuccess(강남역, 양재역, 15, ImmutableList.of(강남역, 양재역));
    }

    @DisplayName("최단 거리 탐색 - 다른 노선")
    @Test
    void findPathSuccess02() {
        findPathSuccess(교대역, 양재역, 26, ImmutableList.of(교대역, 강남역, 양재역));
    }

    private ExtractableResponse<Response> findPathRequest(Long source, Long destination) {

        Map<String, Long> queryParameters = new HashMap<>();
        queryParameters.put("source", source);
        queryParameters.put("target", destination);

        return RestAssured.given().queryParams(queryParameters).log().all()
                          .when().get("/paths")
                          .then().log().all()
                          .extract();
    }

    private void findPathFail(StationResponse source, StationResponse destination) {

        // when
        ExtractableResponse<Response> response = findPathRequest(source.getId(), destination.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void findPathSuccess(StationResponse source, StationResponse destination, int distance, List<StationResponse> path) {

        // when
        PathResponse response = findPathRequest(source.getId(), destination.getId()).as(PathResponse.class);

        // then
        assertThat(response.getDistance()).isEqualTo(distance);
        assertThat(response.getStations()).isEqualTo(path);
    }
}
