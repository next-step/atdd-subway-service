package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;

/**
 * Feature: 지하철 경로 관련 기능
 * <p>
 * Background
 * Given 지하철역 여러개 등록되어 있음
 * And 지하철 노선 여러개 등록되어 있음
 * And 지하철 노선에 지하철역(지하철 구간) 여러개 등록되어 있음
 * And 회원 등록되어 있음
 * And 로그인 되어있음
 * <p>
 * Scenario: 출발역과 도착역 사이의 최단 경로 조회
 * When 지하철 경로 조회 요청
 * Then 출발역과 도착역 사이의 최단 경로 조회됨.
 * And 총 거리도 함께 응답함
 * And ** 지하철 이용 요금도 함께 응답함 **
 * <p>
 * Scenario: 출발역과 도착역이 같은 경우 최단 경로 조회
 * When 지하철 경로 조회 요청
 * Then 최단 경로 조회 실패
 * <p>
 * Scenario: 출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로 조회
 * When 지하철 경로 조회 요청
 * Then 최단 경로 조회 실패
 * <p>
 * Scenario: 존재하지 않은 출발역 또는 도착역으로 최단 경로 조회
 * When 지하철 경로 조회 요청
 * Then 최단 경로 조회 실패
 */
@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 분당선;
    private LineResponse 삼호선;
    private LineResponse 일호선;
    private StationResponse 정자역;
    private StationResponse 양재역;
    private StationResponse 수서역;
    private StationResponse 서현역;
    private StationResponse 소요산역;
    private StationResponse 병점역;
    private String accessToken;

    /**
     * 양재역 ------*3호선(5)*------ 수서역
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * *신분당선(10)*             *분당선(5)*
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * 정쟈역 ------*분당선(5)*------ 서현역
     *
     * 소요산역 ------*일호선(20)*------ 병점역
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        수서역 = StationAcceptanceTest.지하철역_등록되어_있음("수서역").as(StationResponse.class);
        서현역 = StationAcceptanceTest.지하철역_등록되어_있음("서현역").as(StationResponse.class);
        소요산역 = StationAcceptanceTest.지하철역_등록되어_있음("소요산역").as(StationResponse.class);
        병점역 = StationAcceptanceTest.지하철역_등록되어_있음("병점역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("신분당선", "red", 양재역.getId(), 정자역.getId(), 10, 100))
                .as(LineResponse.class);
        분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("분당선", "yellow", 수서역.getId(), 정자역.getId(), 10, 200))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("삼호선", "orange", 양재역.getId(), 수서역.getId(), 5, 100))
                .as(LineResponse.class);
        일호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("일호선", "blue", 소요산역.getId(), 병점역.getId(), 20, 150))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(분당선, 서현역, 정자역, 5);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, 13);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_됨(loginResponse);

        accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 출발역과 도착역 사이의 최단 경로 조회됨.
     * And 총 거리도 함께 응답함
     * And ** 지하철 이용 요금도 함께 응답함 **
     */
    @DisplayName("출발역과 도착역 사이의 최단 경로 조회")
    @Test
    void findShortestPath() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(양재역.getId(), 서현역.getId());

        지하철_최단_경로_조회됨(response);
        지하철_최단_경로_총_거리_조회됨(response, 10);
        지하철_이용_요금_조회됨(response, 880);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회 실패
     */
    @DisplayName("출발역과 도착역이 같은 경우 최단 경로 조회")
    @Test
    void findShortestPathWithException1() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(양재역.getId(), 양재역.getId());

        지하철_최단_경로_조회_실패됨(response);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회 실패
     */
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로 조회")
    @Test
    void findShortestPathWithException2() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(양재역.getId(), 소요산역.getId());

        지하철_최단_경로_조회_실패됨(response);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회 실패
     */
    @DisplayName("존재하지 않은 출발역 또는 도착역으로 최단 경로 조회")
    @Test
    void findShortestPathWithException3() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(양재역.getId(), 0L);

        지하철_최단_경로_조회_실패됨(response);
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(Long upStationId, Long downStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", upStationId);
        params.put("target", downStationId);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .queryParams(params)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_최단_경로_총_거리_조회됨(ExtractableResponse<Response> response, int distance) {
        Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    private void 지하철_이용_요금_조회됨(ExtractableResponse<Response> response, int fare) {
        Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }

    private void 지하철_최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
