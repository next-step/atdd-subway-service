package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private String accessToken;

    /**
     * 교대역    --- 2호선 : 22km ---   강남역
     * |                             |
     * 3호선 : 7km                  신분당선 : 55km
     * |                            |
     * 남부터미널역  --- 3호선 : 2km ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);


        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 55)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 22)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 9)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 7);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> createTokenResponse = 로그인_토큰_생성_요청(tokenRequest);
        로그인_토큰_생성_성공함(createTokenResponse);

        accessToken = createTokenResponse.as(TokenResponse.class).getAccessToken();
    }

    /**
     * Given : 경로 조회 가능한 노선들이 등록되어 있다
     * When : 출발역과 도착역의 경로 조회 요청하면
     * Then : 최단 경로와 거리를 응답한다.
     */
    @DisplayName("지하철 노선의 최단 경로를 조회한다")
    @Test
    void findPaths() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(accessToken, 교대역.getId(), 양재역.getId());

        // then
        지하철_경로_조회됨(response);
        지하철_경로_이름_확인됨(response, Arrays.asList("교대역", "남부터미널역", "양재역"));
    }

    /**
     * When : 출발역과 도착역을 같은 역으로 경로 조회 하면
     * Then : 최단 경로 조회에 실패한다.
     */
    @DisplayName("동일역 경로 조회 예외")
    @Test
    void findPathsSameSourceTargetException() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(accessToken, 교대역.getId(), 교대역.getId());

        // then
        지하철_경로_실패됨(response);
    }

    /**
     * When : 구간에 등록되지 않은 역을 경로에 포함하여 조회하는 경우
     * Then : 최단 경로 조회에 실패한다.
     */
    @DisplayName("구간에 등록되지 않은 역을 경로 조회 하는 경우")
    @Test
    void findPathsUnconnectedException() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(accessToken, 사당역.getId(), 교대역.getId());

        // then
        지하철_경로_실패됨(response);
    }

    /**
     * When : 존재하지 않는 역의 id로 경로 조회 하는 경우
     * Then : 최단 경로 조회에 실패한다.
     */
    @DisplayName("구간에 등록되지 않은 역을 경로 조회 하는 경우")
    @Test
    void findPathsUnenrolledException() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(accessToken, 1000L, 교대역.getId());

        // then
        지하철_경로_실패됨(response);
    }

    /**
     * Scenario: 두 역의 최단 거리 경로를 조회
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리도 함께 응답함
     * And ** 지하철 이용 요금도 함께 응답함 **
     */
    @DisplayName("지하철 경로 검색 시 총 거리와 이용 요금도 함께 응답")
    @Test
    void findPathIntegration() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(accessToken, 교대역.getId(), 양재역.getId());

        // then
        지하철_경로_조회됨(response);
        지하철_경로_이름_확인됨(response, Arrays.asList("교대역", "남부터미널역", "양재역"));

        // and 거리 확인
        지하철_경로_거리_확인됨(response, 9);
        
        // and 요금 확인
        지하철_경로_요금_확인됨(response, 1_250);
    }

    private static void 지하철_경로_요금_확인됨(ExtractableResponse<Response> response, int expectedFare) {
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(expectedFare);
    }

    private static void 지하철_경로_거리_확인됨(ExtractableResponse<Response> response, int expectedDistance) {
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(expectedDistance);
    }

    public static ExtractableResponse<Response> 지하철_경로_조회(String accessToken, Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    private static void 지하철_경로_이름_확인됨(ExtractableResponse<Response> response, List<String> stationNames) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStations())
                .extracting(StationResponse::getName)
                .containsExactlyElementsOf(stationNames);
    }

    public static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_경로_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
