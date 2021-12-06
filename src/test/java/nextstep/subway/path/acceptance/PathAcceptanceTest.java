package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 팔호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 천호역;
    private StationResponse 강동구청;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        천호역 = 지하철역_등록되어_있음("천호").as(StationResponse.class);
        강동구청 = 지하철역_등록되어_있음("강동구청").as(StationResponse.class);

        //String lineName, String color,Long upStationId, Long downStationId, int distance
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10).as(
            LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10).as(
            LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5).as(
            LineResponse.class);
        팔호선 = 지하철_노선_등록되어_있음("팔호선", "bg-red-600", 천호역.getId(), 강동구청.getId(), 5).as(
            LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 최단경로_조회() {
        // when
        ExtractableResponse<Response> response = 경로_조회(강남역.getId(), 남부터미널역.getId());

        // then
    }

    public static ExtractableResponse<Response> 경로_조회(Long source, Long target) {
        Map<String, Long> query = new HashMap<>();
        query.put("source", source);
        query.put("target", target);
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams(query)
            .when().get("/paths")
            .then().log().all().
            extract();
    }

}
