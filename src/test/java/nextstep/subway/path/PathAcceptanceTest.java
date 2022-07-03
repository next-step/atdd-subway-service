package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("지하철 최단 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선*(10) ---  강남역
     * |                              |
     * *3호선*(3)                 *신분당선* (10)
     * |                             |
     * 남부터미널역 --- *3호선*(2) ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(LineRequest.of("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * When 교대역에서 양재역의 최단 경로를 조회하면
     * Then 거리 5, 요금 1250원이 리턴된다
     */
    @DisplayName("10km 이하 최단 경로를 조회한다.")
    @Test
    void getShortestRoute() {
        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(교대역, 양재역);

        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName());

        assertEquals(5, (int) response.jsonPath().get("distance"));
        assertEquals(1250, (int) response.jsonPath().get("fare"));
    }

    /**
     * When 남부터미널에서 강남역의 최단 경로를 조회하면
     * Then 남부터미널, 양재역, 강남역 의 경로와 1,350원의 지하철 이용 요금이 리턴된다
     */
    @DisplayName("10km 초과 50km 까지의 최단 경로와 지하철 요금을 조회한다.")
    @Test
    void getShortestRouteWithSurcharge1() {
        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(남부터미널역, 강남역);

        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(남부터미널역.getName(), 양재역.getName(), 강남역.getName());

        assertEquals(12, (int) response.jsonPath().get("distance"));
        assertEquals(1350, (int) response.jsonPath().get("fare"));
    }

    /**
     * Given 양재역 - 양재시민의숲역의 거리를 50으로 지정하여 추가 후
     * When 양재시민의숲역에서 강남역의 최단 경로를 조회하면
     * Then 양재시민의숲역, 양재역, 강남역 의 경로와 거리 60, 2,250 원의 지하철 이용 요금이 리턴된다
     */
    @DisplayName("50km 초과 최단 경로와 지하철 요금을 조회한다.")
    @Test
    void getShortestRouteWithSurcharge2() {
        // given
        StationResponse 양재시민의숲역 = StationAcceptanceTest.지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲역, 50);

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(양재시민의숲역, 강남역);

        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(양재시민의숲역.getName(), 양재역.getName(), 강남역.getName());

        assertEquals(60, (int) response.jsonPath().get("distance"));
        assertEquals(2250, (int) response.jsonPath().get("fare"));
    }

    /**
     * Given 양재역에서 판교역 노선을 구분당선으로 등록 후 노선 추가 요금 900원을 적용하고
     * When 판교역에서 양재역의 최단 경로를 조회하면
     * Then 거리 5, 요금 2150원이 리턴된다
     */
    /**
     * 교대역    --- *2호선*(10) ---  강남역
     * |                              |
     * *3호선*(3)                 *신분당선* (10)
     * |                             |
     * 남부터미널역 --- *3호선*(2) --- 양재역 --- *구분당선 (5) 노선 추가요금: 900원 * --- 판교역
     */
    @DisplayName("라인 추가 요금을 조회한다.")
    @Test
    void getShortestRouteWithLineSurcharge() {
        StationResponse 판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        지하철_노선_등록되어_있음(LineRequest.of
                ("구분당선", "bg-red-300", 양재역.getId(), 판교역.getId(), 5, 900)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(양재역, 판교역);


        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(양재역.getName(), 판교역.getName());

        assertEquals(5, (int) response.jsonPath().get("distance"));
        assertEquals(2150, (int) response.jsonPath().get("fare"));
    }

    /**
     * Given 양재역에서 양재시민의숲역 노선을 구분당선으로 등록 후 노선 추가 요금 900원을 적용하고
     *       And 판교역에서 이매역 노선을 경강선으로 등록 후 노선 추가 요금 1000원을 적용한 후
     * When 양재역에서 이매역의 최단 경로를 조회하면
     * Then 양재역, 판교역, 이매역 경로와 2,350원의 지하철 이용 요금이 리턴된다
     */
    /**
     * 교대역    --- *2호선*(10) ---  강남역                                          이매역
     * |                              |                                                |
     * *3호선*(3)                 *신분당선* (10)                                 * 경강선 * (10) 추가요금: 1000원
     * |                             |                                                |
     * 남부터미널역 --- *3호선*(2) --- 양재역 --- *구분당선 (5) 추가요금: 900원 * --- 판교역
     */
    @DisplayName("추가 요금이 있는 노선을 환승하는 경우 가장 높은 금액이 적용된다.")
    @Test
    void getShortestRouteWithMultipleLineSurcharge() {
        // given
        StationResponse 판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        지하철_노선_등록되어_있음(LineRequest.of
                ("구분당선", "bg-red-300", 양재역.getId(), 판교역.getId(), 5, 900)).as(LineResponse.class);

        StationResponse 이매역 = StationAcceptanceTest.지하철역_등록되어_있음("이매역").as(StationResponse.class);
        지하철_노선_등록되어_있음(LineRequest.of
                ("경강선", "bg-blue-300", 판교역.getId(), 이매역.getId(), 10, 1000)).as(LineResponse.class);
        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(양재역, 이매역);

        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(양재역.getName(), 판교역.getName(), 이매역.getName());

        assertEquals(15, (int) response.jsonPath().get("distance"));
        assertEquals(2350, (int) response.jsonPath().get("fare"));
    }

    /**
     * Given 청소년이 로그인한 상태에서
     * When 교대역에서 양재역의 최단 경로를 조회하면
     * Then 거리 5, 요금 1070원이 리턴된다
     */
    @DisplayName("10km 이하 거리의 청소년 요금을 조회한다")
    @Test
    void getShortestRouteAndFareForTeenagers() {
        // given
        final String EMAIL = "teenager@email.com";
        final String PASSWORD = "teenager12";
        final Integer AGE = 13;
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        String token = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(token, 교대역, 양재역);

        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName());

        assertEquals(5, (int) response.jsonPath().get("distance"));
        assertEquals(1070, (int) response.jsonPath().get("fare"));
    }

    /**
     * Given 어린이로 로그인한 상태에서
     * When 교대역에서 양재역의 최단 경로를 조회하면
     * Then 거리 5, 요금 800원이 리턴된다
     */
    @DisplayName("10km 이하 거리의 어린이 요금을 조회한다")
    @Test
    void getShortestRouteAndFareForChildren() {
        // given
        final String EMAIL = "children@email.com";
        final String PASSWORD = "children12";
        final Integer AGE = 10;
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        String token = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(token, 교대역, 양재역);

        // then
        List<String> stations = response.jsonPath().get("stations.name");
        assertThat(stations).containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName());

        assertEquals(5, (int) response.jsonPath().get("distance"));
        assertEquals(800, (int) response.jsonPath().get("fare"));
    }

    /**
     * When 출발역과 도착역이 같은 경우
     * Then 오류가 발생한다
     */
    @DisplayName("출발역과 도착역이 같은 경우 오류가 발생한다.")
    @Test
    void hasSameSourceAndTargetStation() {
        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(교대역, 교대역);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.statusCode());
    }

    /**
     * When 출발역과 도착역이 연결되어 있지 않은 경우
     * Then 오류가 발생한다
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 오류가 발생한다.")
    @Test
    void isNotConnected() {
        StationResponse 시청역 = 지하철역_등록되어_있음("시청역").as(StationResponse.class);
        StationResponse 종각역 = 지하철역_등록되어_있음("종각역").as(StationResponse.class);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(LineRequest.of(
                "일호선", "bg-red-600", 시청역.getId(), 종각역.getId(), 3)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(종각역, 교대역);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.statusCode());
    }

    /**
     * When 존재하지 않는 출발역을 조회할 경우
     * Then 오류가 발생한다
     */
    @DisplayName("존재하지 않는 출발역을 조회할 경우 오류가 발생한다.")
    @Test
    void isNotPresentSourceStation() {
        StationResponse 시청역 = 지하철역_등록되어_있음("시청역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(시청역, 교대역);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.statusCode());
    }

    /**
     * When 존재하지 않는 도착역을 조회할 경우
     * Then 오류가 발생한다
     */
    @DisplayName("존재하지 않는 도착역을 조회할 경우 오류가 발생한다.")
    @Test
    void isNotPresentTargetStation() {
        StationResponse 시청역 = 지하철역_등록되어_있음("시청역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 노선_최단경로_조회(교대역, 시청역);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.statusCode());
    }

    private ExtractableResponse<Response> 노선_최단경로_조회(String token, StationResponse 출발역, StationResponse 도착역) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", 출발역.getId(), 도착역.getId())
                .then().log().all()
                .extract();
    }
    private ExtractableResponse<Response> 노선_최단경로_조회(StationResponse 출발역, StationResponse 도착역) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", 출발역.getId(), 도착역.getId())
                .then().log().all()
                .extract();
    }

}
