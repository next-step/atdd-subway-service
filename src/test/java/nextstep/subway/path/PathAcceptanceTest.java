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
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 동작역;
    private StationResponse 석촌역;
    private StationResponse 남부터미널역;

    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 구호선;
    private LineResponse 신분당선;


    /**
     * 교대역 -------- 2호선(10) ------ 강남역
     * ㅣ                               ㅣ
     * ㅣ                               ㅣ
     * 3호선(3)                      신분당선(10)
     * ㅣ                               ㅣ
     * ㅣ                               ㅣ
     * 남부터미널역 ------3호선(2) ------- 양재
     * <p>
     * 동작역 --------- 9호선(13) ------ 석촌역
     */


    @BeforeEach
    public void setup() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        동작역 = StationAcceptanceTest.지하철역_등록되어_있음("동작역").as(StationResponse.class);
        석촌역 = StationAcceptanceTest.지하철역_등록되어_있음("석촌역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("구호선", "bg-red-600", 동작역.getId(), 석촌역.getId(), 13)).as(LineResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분단성", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("출발역과 도착역 사이 최단 경로 조회")
    @Test
    public void 최단경로_조회() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역.getId(), 양재역.getId());
        // then
        최단경로_조회_검증(response, 5);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외발생")
    @Test
    public void 최단경로조회_예외발생1() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(양재역.getId(), 양재역.getId());
        // then
        최단_경로_조회_실패_검증(response.statusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외발생")
    @Test
    public void 최단경로조회_에외발생2() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(교대역.getId(), 동작역.getId());
        // then
        최단_경로_조회_실패_검증(response.statusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우")
    @Test
    public void 최단경로조회_에외발생3() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(양재역.getId(), 0L);
        // then
        최단_경로_조회_실패_검증(response.statusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 최단경로_조회_검증(ExtractableResponse<Response> response, int distance) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance)
        );
    }

    private static void 최단_경로_조회_실패_검증(int response, int BAD_REQUEST) {
        assertThat(response).isEqualTo(BAD_REQUEST);
    }


    private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(Long startStationId, Long endStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", startStationId);
        params.put("target", endStationId);

        return RestAssured.given().log().all()
                .queryParams(params)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

}
