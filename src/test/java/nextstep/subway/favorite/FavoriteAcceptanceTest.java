package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "meeingjae@mingvel.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 29;

    StationResponse 삼전역;
    StationResponse 종합운동장역;
    StationResponse 잠실새내역;
    StationResponse 석촌고분역;

    LineResponse 신분당선;

    String 인증_토큰;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @BeforeEach
    public void before() {
        super.setUp();
        // 지하철역 등록되어 있음
        삼전역 = 지하철역_생성_요청("삼전역").as(StationResponse.class);
        종합운동장역 = 지하철역_생성_요청("종합운동장역").as(StationResponse.class);
        잠실새내역 = 지하철역_생성_요청("잠실새내역").as(StationResponse.class);
        석촌고분역 = 지하철역_생성_요청("석촌고분역").as(StationResponse.class);
        // 지하철 노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 삼전역.getId(), 종합운동장역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        // 지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록_요청(신분당선, 종합운동장역, 잠실새내역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 잠실새내역, 석촌고분역, 10);
        // 회원 등록되어 있음
        Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
        회원_생성을_요청(사용자.getEmail(), 사용자.getPassword(), 사용자.getAge());
        // 로그인 되어있음
        인증_토큰 = 인증_요청(new TokenRequest(사용자.getEmail(), 사용자.getPassword())).as(TokenResponse.class).getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     * <p>
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     * <p>
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 관리")
    @Test
    void 즐겨찾기_관리() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(new FavoriteRequest(삼전역.getId(), 잠실새내역.getId()));
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회됨(getResponse, 삼전역.getName(), 잠실새내역.getName());

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse);
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void 즐겨찾기_생성() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(new FavoriteRequest(삼전역.getId(), 잠실새내역.getId()));

        즐겨찾기_생성됨(response);
    }

    /**
     * Given 즐겨찾기 생성 요청
     * And  즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void 즐겨찾기_목록_조회() {

        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(new FavoriteRequest(삼전역.getId(), 잠실새내역.getId()));

        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();

        즐겨찾기_목록_조회됨(getResponse, 삼전역.getName(), 잠실새내역.getName());
    }

    /**
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 비었음
     */
    @DisplayName("즐겨찾기 관리 목록 조회 - 빈 목록 ")
    @Test
    void 즐겨찾기_목록_조회_빈_목록() {
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        즐겨찾기_목록_비었음(response);

    }

    /**
     * Given 즐겨찾기 생성 요청
     * And  즐겨찾기 생성됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void 즐겨찾기_삭제() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(new FavoriteRequest(삼전역.getId(), 잠실새내역.getId()));

        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse);

        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(FavoriteRequest request) {
        return RestAssured.given().log().all()
                .auth().oauth2(인증_토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2(인증_토큰)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return RestAssured.given().log().all()
                .auth().oauth2(인증_토큰)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> getResponse, String source, String target) {
        FavoriteResponse response = getResponse.as(FavoriteResponse.class);
        assertThat(response.size()).isEqualTo(1);

        response.getFavoriteSections().stream().findFirst().ifPresent(it -> {
            assertThat(it.getId()).isEqualTo(1);
            assertThat(it.sourceStationName()).isEqualTo(source);
            assertThat(it.targetStationName()).isEqualTo(target);
        });
    }

    private void 즐겨찾기_목록_비었음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}