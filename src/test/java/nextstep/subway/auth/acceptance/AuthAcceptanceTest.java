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

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String INVALID_EMAIL = "invalid-email";
    public static final String INVALID_PASSWORD = "invalid-password";
    public static final String INVALID_TOKEN = "invalid-token";

    @BeforeEach
    void setup() {
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공(로그인_응답);

        TokenResponse tokenResponse = 로그인_응답.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();

        // when
        ExtractableResponse<Response> 토큰_로그인_응답 = 토큰_로그인_요청(tokenResponse.getAccessToken());

        // then
        로그인_성공(토큰_로그인_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패 - Email 불일치")
    @Test
    void myInfoWithBadBearerAuth_email() {
        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(INVALID_EMAIL, PASSWORD);

        // then
        로그인_실패(로그인_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패 - PW 불일치")
    @Test
    void myInfoWithBadBearerAuth_password() {
        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, INVALID_PASSWORD);

        // then
        로그인_실패(로그인_응답);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공(로그인_응답);

        // when
        ExtractableResponse<Response> 토큰_로그인_응답 = 토큰_로그인_요청(INVALID_TOKEN);

        // then
        로그인_실패(토큰_로그인_응답);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰_로그인_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
