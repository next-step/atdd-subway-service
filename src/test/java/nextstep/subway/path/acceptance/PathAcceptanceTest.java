package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

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

        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");

        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 15);
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

    @DisplayName("")
    @Test
    void findPathTest01() {

    }

    @DisplayName("")
    @Test
    void findPathTest02() {

    }

    public ExtractableResponse<Response> findPath(int source, int destination) {

        Map<String, Integer> queryParameters = new HashMap<>();
        queryParameters.put("source", source);
        queryParameters.put("target", destination);

        // when
        return RestAssured.given().queryParams(queryParameters).log().all()
                          .when().get("/paths")
                          .then().log().all()
                          .extract();
    }
}
