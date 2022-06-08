package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

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

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 최단경로찾기(){
        StationResponse 출발역 =  교대역;
        StationResponse 도착역 =  양재역;

        ExtractableResponse<Response> response = 도착역으로가는_최단경로를_조회한다(교대역,양재역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ShortestPathResponse shortestPathResponse = response.as(ShortestPathResponse.class);
        List<StationResponse> stations = shortestPathResponse.getStations();
        assertThat(stations)
                .hasSize(3)
                .containsExactly(교대역,남부터미널역,양재역);
    }

    public ExtractableResponse<Response> 도착역으로가는_최단경로를_조회한다(StationResponse source, StationResponse target) {
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("source",String.valueOf(source.getId()));
        queryParams.put("target",String.valueOf(target.getId()));

        return RestAssured
                    .given().log().all()
                        .accept(ContentType.JSON)
                        .queryParams(queryParams)
                    .when().get("/paths")
                    .then().log().all()
                    .extract();
        }
}
