package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private TokenRequest 인증토큰;
    private TokenRequest 잘못된_인증토큰;
    private String USER_EMAIL = "test@email.com";
    private String USER_PASSWORD = "password";
    private int USER_AGE = 20;

    private String INVALID_USER_EMAIL = "invalid@email.com";

    @BeforeEach
    public void setUp() {
        super.setUp();

        MemberAcceptanceTest.회원_생성을_요청(USER_EMAIL, USER_PASSWORD, USER_AGE);
        인증토큰 = new TokenRequest(USER_EMAIL, USER_PASSWORD);
        잘못된_인증토큰 = new TokenRequest(INVALID_USER_EMAIL, USER_PASSWORD);

    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // 로그인 요청
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(인증토큰);

        // 로그인결과 확인
        로그인_성공(로그인응답);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // 로그인 요청
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(잘못된_인증토큰);

        // 로그인결과 확인
        로그인_실패(로그인응답);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(인증토큰);

        ExtractableResponse<Response> 유효하지않은_토큰정보로_요청 = 유효하지않은_토큰정보로_요청(로그인응답);

        인증과정에서_실패(유효하지않은_토큰정보로_요청);
    }

    private void 인증과정에서_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all().
                        extract();
    }

    private ExtractableResponse<Response> 유효하지않은_토큰정보로_요청(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "Invalid")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all().
                        extract();
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
