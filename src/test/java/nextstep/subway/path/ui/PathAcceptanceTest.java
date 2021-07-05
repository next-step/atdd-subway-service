package nextstep.subway.path.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰을_요청한다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 구리역;
    private StationResponse 도농역;
    private LineResponse 중앙선;
    private StationResponse 용산역;

    private final String A_EMAIL = "a@naver.com";
    private final String B_EMAIL = "b@naver.com";
    private final String C_EMAIL = "c@naver.com";
    private final String PASSWORD = "password";
    private final int ADULT_AGE = 20, TEENAGER_AGE = 15, CHILD_AGE = 10;
    private TokenResponse 어른_토큰;
    private TokenResponse 청소년_토큰;
    private TokenResponse 어린이_토큰;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);
        교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_생성_요청("남부터미널역").as(StationResponse.class);

        구리역 = 지하철역_생성_요청("구리역").as(StationResponse.class);
        도농역 = 지하철역_생성_요청("도농역").as(StationResponse.class);
        용산역 = 지하철역_생성_요청("용산역").as(StationResponse.class);
        중앙선 = 지하철_노선_생성_요청(new LineRequest("중앙선", "bg-red-300", 구리역.getId(), 도농역.getId(), 100)).as(LineResponse.class);

        신분당선 = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-300", 강남역.getId(), 양재역.getId(), 15)).as(LineResponse.class);
        이호선 = 지하철_노선_생성_요청(new LineRequest("이호선", "bg-red-400", 교대역.getId(), 강남역.getId(), 12)).as(LineResponse.class);
        삼호선 = 지하철_노선_생성_요청(new LineRequest("삼호선", "bg-red-500", 교대역.getId(), 양재역.getId(), 27)).as(LineResponse.class);
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 9));

        회원_생성을_요청(A_EMAIL, PASSWORD, ADULT_AGE);
        회원_생성을_요청(B_EMAIL, PASSWORD, TEENAGER_AGE);
        회원_생성을_요청(C_EMAIL, PASSWORD, CHILD_AGE);
        TokenRequest aRequest = new TokenRequest(A_EMAIL, PASSWORD);
        TokenRequest bRequest = new TokenRequest(B_EMAIL, PASSWORD);
        TokenRequest cRequest = new TokenRequest(C_EMAIL, PASSWORD);
        어른_토큰 = 토큰을_요청한다(aRequest).as(TokenResponse.class);
        청소년_토큰 = 토큰을_요청한다(bRequest).as(TokenResponse.class);
        어린이_토큰 = 토큰을_요청한다(cRequest).as(TokenResponse.class);

    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void fail_test1() {
        최단_경로가_조회되지_않음(최단_경로를_조회(어른_토큰, 강남역.getId(), 강남역.getId()));
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void fail_test2() {
        최단_경로가_조회되지_않음(최단_경로를_조회(어른_토큰, 강남역.getId(), 구리역.getId()));
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void fail_test3() {
        최단_경로가_조회되지_않음(최단_경로를_조회(어른_토큰, 강남역.getId(), 용산역.getId()));
    }

    private void 최단_경로가_조회되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void getShortestPath() {
        ExtractableResponse<Response> response = 최단_경로를_조회(어른_토큰, 강남역.getId(), 남부터미널역.getId());
        최단_경로가_조회됨(response, 21, 1250);
    }

    @DisplayName("어른이 최단 경로를 조회할 경우 공제는 없다")
    @Test
    void adult_getShortestPath() {
        ExtractableResponse<Response> response = 최단_경로를_조회(어른_토큰, 강남역.getId(), 남부터미널역.getId());
        최단_경로가_조회됨(response, 21, 1250);
    }

    @DisplayName("청소년 운임은 350원을 공제한 금액의 20%할인이 된다")
    @Test
    void teenager_getShortestPath() {
        ExtractableResponse<Response> response = 최단_경로를_조회(청소년_토큰, 강남역.getId(), 남부터미널역.getId());
        최단_경로가_조회됨(response, 21, 720);
    }

    @DisplayName("어린이 운임은 350원을 공제한 금액의 50%할인이 된다")
    @Test
    void child_getShortestPath() {
        ExtractableResponse<Response> response = 최단_경로를_조회(어린이_토큰, 강남역.getId(), 남부터미널역.getId());
        최단_경로가_조회됨(response, 21, 450);
    }

    private ExtractableResponse<Response> 최단_경로를_조회(TokenResponse tokenResponse, long source, long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static void 최단_경로가_조회됨(ExtractableResponse response, int distance, int fee) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse as = response.as(PathResponse.class);
        assertThat(as.getDistance()).isEqualTo(distance);
        assertThat(as.getFee()).isEqualTo(fee);
    }
}