package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


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
     * 교대역      --- *2호선* ---       강남역
     * |                                 |
     * *3호선*                          *신분당선*
     * |                                 |
     * 남부터미널  --- *3호선* ---        양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "red lighten-1", 강남역, 양재역, 10, 900).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("2호선", "green lighten-1", 교대역, 강남역, 10, 0).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "orange darken-1", 교대역, 양재역, 5, 500).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    public ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color, StationResponse station1, StationResponse station2, int distance, int addFee) {
        LineRequest params = new LineRequest(name, color, station1.getId(), station2.getId(), distance);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse station1, StationResponse station2, int distance) {
        SectionRequest params = new SectionRequest(station1.getId(), station2.getId(), distance);
        String uri = "/lines/" + line.getId() + "/sections";
        RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("최단 경로 조회")
    void case1() {
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", "4");

        ExtractableResponse<Response> response = 최단경로_요청(params);

        최단경로_요청이_조회됨(response);
    }

    @Test
    @DisplayName("최단 경로 조회 예외처리 : 출발지 도착지 같을 경우")
    void case2() {
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", "1");

        ExtractableResponse<Response> response = 최단경로_요청(params);

        최단경로_요청_실패됨(response);
    }

    @Test
    @DisplayName("최단 경로 조회 예외처리 : 도착치가 존재하지 않은 경우")
    void case3() {
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", "6");

        ExtractableResponse<Response> response = 최단경로_요청(params);

        최단경로_요청_실패됨(response);
    }

    @Test
    @DisplayName("최단 경로 조회 요금조회")
    void case14() {
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", "4");

        ExtractableResponse<Response> response = 최단경로_요청(params);

        최단경로_요청이_조회됨(response);

        PathResponse result = 최단경로_요청(params).as(PathResponse.class);
        assertThat(result.getDistance()).isEqualTo(12);
        assertThat(result.getTotalFee()).isEqualTo(1250);

    }

    private ExtractableResponse<Response> 최단경로_요청(Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then()
                .extract();
        return response;
    }

    private void 최단경로_요청이_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단경로_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
