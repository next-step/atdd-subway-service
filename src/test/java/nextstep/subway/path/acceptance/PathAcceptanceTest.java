package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.path.dto.StationsResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역")
                .as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역")
                .as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역")
                .as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역")
                .as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-300", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-yellow-400", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-green-500", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 교대역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 7);
    }

    /**
     * Given 2개 이상의 지하철 역이 등록 되어 있음
     * And 한 개 이상의 지하철 노선이 등록 되어 있음
     * And 지하철 노선에 지하철 역이 등록 되어 있음
     * When 출발역과 도착역을 선택하면
     * Then 최단거리가 조회된다.
     * (deatil : 강남 - 남부터미널 간 최단경로를 조회한다.)
     */
    @Test
    @DisplayName("최단경로 조회")
    void 최단경로조회() {
        // when
        Map<String, Long> params = new HashMap<>();
        params.put("source", 강남역.getId());
        params.put("target", 남부터미널역.getId());

        ExtractableResponse<Response> response = 최단경로조회_요청(params);

        // then
        ShortestPathResponse shortestPathResponse = response.as(ShortestPathResponse.class);
        ShortestPathResponse answerPath = new ShortestPathResponse(
                new StationsResponse(
                        Arrays.asList(
                                new nextstep.subway.path.dto.StationResponse(강남역.getId(), 강남역.getName(), 강남역.getCreatedDate()),
                                new nextstep.subway.path.dto.StationResponse(교대역.getId(), 교대역.getName(), 교대역.getCreatedDate()),
                                new nextstep.subway.path.dto.StationResponse(남부터미널역.getId(), 남부터미널역.getName(), 남부터미널역.getCreatedDate())
                        )
                ),
                13
        );
        최단경로조회_검증(shortestPathResponse, answerPath);
    }

    /**
     * Given 2개 이상의 지하철 역이 등록 되어 있음
     * And 한 개 이상의 지하철 노선이 등록 되어 있음
     * And 지하철 노선에 지하철 역이 등록 되어 있음
     * When 출발역과 도착역을 동일하게 설정하면
     * Then 최단거리를 조회 할 수 없다. (에러 응답)
     */
    @Test
    @DisplayName("출발역, 도착역이 같을 경우 에러 응응답 확인")
    void 출발역_도착역_동일_에러_테스트() {
        // when
        Map<String, Long> params = new HashMap<>();
        params.put("source", 강남역.getId());
        params.put("target", 강남역.getId());
        ExtractableResponse<Response> response = 최단경로조회_요청(params);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    /**
     * Given 2개 이상의 지하철 역이 등록 되어 있음
     * And 한 개 이상의 지하철 노선이 등록 되어 있음
     * And 지하철 노선에 지하철 역이 등록 되어 있음
     * When 출발역과 도착역이 연결 되어 있지 않음
     * Then 최단거리를 조회 할 수 없다. (에러 응답)
     */
    @Test
    @DisplayName("출발역, 도착역이 연결 되어 있지 않는 경우 에러 응답")
    void 출발역_도착역_연결안됨_에러_테스트() {
        // when
        StationResponse 까치산역 = StationAcceptanceTest.지하철역_등록되어_있음("까치산역")
                .as(StationResponse.class);

        StationResponse 서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역")
                .as(StationResponse.class);

        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("1호선", "bg-blue-600", 까치산역.getId(), 서울역.getId(), 10))
                .as(LineResponse.class);

        // then
        Map<String, Long> params = new HashMap<>();
        params.put("source", 강남역.getId());
        params.put("target", 서울역.getId());
        ExtractableResponse<Response> response = 최단경로조회_요청(params);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    private ExtractableResponse<Response> 최단경로조회_요청(Map<String, Long> params) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().queryParams(params).get("/paths")
                .then().log().all()
                .extract();

        return response;
    }

    private void 최단경로조회_검증(ShortestPathResponse shortestPathResponse, ShortestPathResponse answerPath) {
        List<nextstep.subway.path.dto.StationResponse> responseStations = shortestPathResponse.getStations().getStations();
        List<nextstep.subway.path.dto.StationResponse> answerStations = answerPath.getStations().getStations();

        assertAll(
                () -> assertThat(responseStations.get(0).getName()).isEqualTo(answerStations.get(0).getName()),
                () -> assertThat(responseStations.get(1).getName()).isEqualTo(answerStations.get(1).getName()),
                () -> assertThat(responseStations.get(2).getName()).isEqualTo(answerStations.get(2).getName()),
                () -> assertThat(shortestPathResponse.getDistance()).isEqualTo(answerPath.getDistance())
        );
    }
}
