package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "probitanima11@gmail.com";
    private static final String PASSWORD = "11";
    private static final String WRONG_PASSWORD = "22";
    private static final int AGE = 30;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        TokenRequest params = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(params);
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        ExtractableResponse<Response> authResponse = 토큰_인증(tokenResponse);

        // then
        로그인_됨(loginResponse);
        토큰인증_성공(authResponse);
        회원_정보_조회됨(authResponse, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        TokenRequest params = new TokenRequest(EMAIL, WRONG_PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(params);

        // then
        로그인_실패됨(loginResponse);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        TokenRequest params = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(params);
        TokenResponse tokenResponse = new TokenResponse("invalid");
        ExtractableResponse<Response> authResponse = 토큰_인증(tokenResponse);

        // when
        로그인_됨(loginResponse);
        토큰인증_실패(authResponse);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest params = new TokenRequest(email, password);
        return 로그인_요청(params);
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all().
                        extract();
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 토큰_인증(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 토큰인증_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    public static void 토큰인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
}
