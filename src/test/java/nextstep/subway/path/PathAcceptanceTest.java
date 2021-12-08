package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

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
     * 교대역    --- *2호선* --- 강남역
     * |                       |
     * *3호선*                  *신분당선*
     * |                       |
     * 남부터미널역   --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 0, 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-green-600", 0, 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-orange-600", 0, 교대역.getId(), 양재역.getId(), 5))
            .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void getShortestPaths() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("source", 강남역.getId().toString());
        params.put("target", 남부터미널역.getId().toString());
        ExtractableResponse<Response> response = 최단_경로_목록_요청(
            params);

        // then
        최단_경로_목록_응답됨(response);
    }

    private ExtractableResponse<Response> 최단_경로_목록_요청(Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .params(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/paths")
            .then().log().all().extract();
        return response;
    }

    private void 최단_경로_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
