package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 기능 관련 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {
    private String email;
    private String password;
    private Integer age;

    @BeforeEach
    public void setUp() {
        super.setUp();

        email = "yomni@gmail.com";
        password = "password";
        age = 99;

        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(email, password, age);
        ExtractableResponse<Response> findResponse = MemberAcceptanceTest.회원_정보_조회_요청(createResponse);
        MemberAcceptanceTest.회원_정보_조회됨(findResponse, email, age);
    }

    /**
     * Given : 회원 등록되어 있음
     * When : 로그인 요청
     * Then : 로그인 됨
     */
    @DisplayName("Bearer Auth 로그인 시도")
    @Test
    void myInfoWithBearerAuth() {
        // when
        TokenRequest tokenRequest = new TokenRequest(email, password);
        ExtractableResponse<Response> response = 로그인_토큰_생성_요청(tokenRequest);

        // then
        로그인_토큰_생성_성공함(response);
    }

    /**
     * Given : 회원 등록되어 있음
     * When : 등록되어 있는 회원 정보와는 상이한 정보로 로그인 요청
     * Then : 토큰 생성 시 예외 발생
     */
    @DisplayName("Bearer Auth 로그인 실패 - email 주소 정보 다름")
    @Test
    void myInfoWithBadBearerAuth1() {
        // when
        email = "year@gmail.com";
        TokenRequest tokenRequest = new TokenRequest(email, password);
        ExtractableResponse<Response> response = 로그인_토큰_생성_요청(tokenRequest);

        // then
        로그인_토큰_생성_실패됨(response);
    }

    /**
     * Given : 회원 등록되어 있음
     * When : 등록되어 있는 회원 정보와는 상이한 정보로 로그인 요청
     * Then : 토큰 생성 시 예외 발생
     */
    @DisplayName("Bearer Auth 로그인 실패 - password 정보 다름")
    @Test
    void myInfoWithBadBearerAuth2() {
        // when
        password = "wrongPassword";
        TokenRequest tokenRequest = new TokenRequest(email, password);
        ExtractableResponse<Response> response = 로그인_토큰_생성_요청(tokenRequest);

        // then
        로그인_토큰_생성_실패됨(response);
    }


    // members/me 로 토큰으로 로그인 시도 --> 예외 케이스

    /**
     * Given : accessToken 발급 완료 됨
     * When : accessToken으로 로그인 요청
     * Then : 로그인 됨
     */
    @DisplayName("accessToken을 이용한 로그인 시도")
    @Test
    void myInfoWithAccessToken() {
        // given
        TokenRequest tokenRequest = new TokenRequest(email, password);
        String accessToken = 로그인_토큰_생성_요청(tokenRequest).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 로그인_AccessToken_으로_요청(accessToken);

        // then
        로그인_AccessToken_으로_성공함(response, tokenRequest.getEmail());
    }

    /**
     * When : 잘못된 accessToken으로 로그인 요청
     * Then : 로그인 실패
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String accessToken = "fooAccessToken";

        // when
        ExtractableResponse<Response> response = 로그인_AccessToken_으로_요청(accessToken);

        // then
        로그인_AccessToken_으로_실패됨(response);
    }

    private static ExtractableResponse<Response> 로그인_토큰_생성_요청(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private static void 로그인_토큰_생성_성공함(ExtractableResponse<Response> response) {
        TokenResponse token = response.as(TokenResponse.class);
        assertThat(token).isNotNull();
    }

    private static void 로그인_토큰_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
    private static ExtractableResponse<Response> 로그인_AccessToken_으로_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private void 로그인_AccessToken_으로_성공함(ExtractableResponse<Response> response, String email) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse member = response.as(MemberResponse.class);
        assertThat(member.getEmail()).isEqualTo(email);
    }

    private void 로그인_AccessToken_으로_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
