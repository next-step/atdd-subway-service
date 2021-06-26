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
    private static final String EMAIL = "psj6414@naver.com";
    private static final String PASSWORD = "#1234";
    private static final Integer AGE = 30;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 회원_정보_요청(tokenResponse);
        MemberAcceptanceTest.회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, "1111");
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = 유효하지_않은_토큰생성();
        토큰이_유효하지_않음(tokenResponse);
    }

    public static TokenResponse 로그인_되어_있음(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        return response.as(TokenResponse.class);
    }

    private static ExtractableResponse<Response> 로그인_요청(TokenRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }


    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private TokenResponse 유효하지_않은_토큰생성() {
        return new TokenResponse("accessToken");
    }

    public static ExtractableResponse<Response> 회원_정보_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all().
                auth().oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();
    }

    private void 토큰이_유효하지_않음(TokenResponse tokenResponse) {
        RestAssured.given().log().all().
                auth().oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
