package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String YOUTH_EMAIL = "youthl@email.com";
    private static final int YOUTH_AGE = 18;

    private static final String EMAIL = "email@email.com";
    private static final int AGE = 20;

    private static final String PASSWORD = "password";


    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> 회원_생성을_요청 = MemberAcceptanceTest
                .회원_생성을_요청(EMAIL, PASSWORD, AGE);

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 5, 1000)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 0)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 남부터미널역, 15);
        삼호선 = LineAcceptanceTest.지하철_노선_조회_요청(삼호선).as(LineResponse.class);
    }

    @Test
    @DisplayName("로그인 회원 최단 거리 조회")
    void shortest_path() {
        // when
        final ExtractableResponse<Response> 로그인_요청 = AuthAcceptanceTest.로그인_요청(TokenRequest.of(EMAIL, PASSWORD));
        // then
        final String accessToken = AuthAcceptanceTest.로그인_성공됨(로그인_요청);
        // when
        PathResponse 최단거리_조회됨 = 지하철_최단거리_조회(accessToken, 강남역.getId(), 남부터미널역.getId()).as(PathResponse.class);
        // then
        assertThat(최단거리_조회됨.getStations()).hasSize(3);
        assertThat(최단거리_조회됨.getDistance()).isEqualTo(20);
        assertThat(최단거리_조회됨.getFare()).isEqualTo(2450);
    }

    public static ExtractableResponse<Response> 지하철_최단거리_조회(String accessToken, Long source,  Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/paths?source={source}&target={target}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 지하철_최단거리_조회됨(ExtractableResponse<Response> response) {
        // when
        final PathResponse 최단거리_조회됨 = response.as(PathResponse.class);
        // then
        assertThat(최단거리_조회됨.getStations()).hasSize(3);
        assertThat(최단거리_조회됨.getDistance()).isEqualTo(20);
    }

    @Test
    @DisplayName("비회원 최단 거리 조회")
    void shortestPath() {
        // when
        final ExtractableResponse<Response> 지하철_최단거리_조회 = 비회원_지하철_최단거리_조회(강남역.getId(), 남부터미널역.getId());
        // then
        비회원_지하철_최단거리_조회_실패(지하철_최단거리_조회);
    }

    @Test
    @DisplayName("청소년 최단 거리 조회")
    void teenagerShortestPath() {
        //given
        ExtractableResponse<Response> 청소년_회원_생성을_요청 = MemberAcceptanceTest.회원_생성을_요청(YOUTH_EMAIL, PASSWORD, YOUTH_AGE);
        final ExtractableResponse<Response> 로그인_요청 = AuthAcceptanceTest.로그인_요청(TokenRequest.of(YOUTH_EMAIL, PASSWORD));
        final String accessToken = AuthAcceptanceTest.로그인_성공됨(로그인_요청);
        // when
        final ExtractableResponse<Response> 지하철_최단거리_조회 = 회원_지하철_최단거리_조회(강남역.getId(), 남부터미널역.getId(), accessToken);
        // then
        청소년_회원_지하철_최단거리_조회됨(지하철_최단거리_조회);
    }

    public static ExtractableResponse<Response> 비회원_지하철_최단거리_조회(final Long source, final Long target) {
        return RestAssured.given().log().all()
                .when()
                .get("/paths?source={source}&target={target}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 비회원_지하철_최단거리_조회_실패(ExtractableResponse<Response> response) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 회원_지하철_최단거리_조회(final Long source, final Long target, final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/paths?source={source}&target={target}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 청소년_회원_지하철_최단거리_조회됨(ExtractableResponse<Response> response) {
        // when
        final PathResponse 최단거리_조회됨 = response.as(PathResponse.class);
        // then
        assertThat(최단거리_조회됨.getStations()).hasSize(3);
        assertThat(최단거리_조회됨.getDistance()).isEqualTo(20);
        assertThat(최단거리_조회됨.getFare()).isEqualTo(1680);
    }


}
