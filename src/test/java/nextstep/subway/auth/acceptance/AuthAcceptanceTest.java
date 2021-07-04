package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.common.Constants.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.common.dto.ErrorResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인 성공(Bearer Auth)")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        // then
        로그인_성공(response);

        // when
        TokenResponse tokenResponse = response.jsonPath().getObject(".", TokenResponse.class);
        response = 로그인_사용자_일치_확인_요청(tokenResponse);

        // then
        로그인_사용자_일치_확인_성공(response, EMAIL);
    }

    @DisplayName("로그인 실패(Bearer Auth)")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String invalidEmail = "test@abc.com";
        String invalidPassword = "1234";

        // when
        ExtractableResponse<Response> response = 로그인_요청(invalidEmail, PASSWORD);
        // then
        로그인_실패_유효_하지_않은_이메일(response);

        // when
        response = 로그인_요청(EMAIL, invalidPassword);
        // then
        로그인_실패_유효_하지_않은_비밀번호(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY";
        TokenResponse tokenResponse = new TokenResponse(invalidToken);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_일치_확인_요청(tokenResponse);

        // then
        로그인_사용자_확인_토큰_불일치_실패(response);
    }

    private ExtractableResponse<Response> 로그인_사용자_일치_확인_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                            .auth().oauth2(tokenResponse.getAccessToken())
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().get("/members/me")
                            .then().log().all()
                            .extract();
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_사용자_일치_확인_성공(ExtractableResponse<Response> response, String email) {
        MemberResponse memberResponse = response.jsonPath().getObject(".", MemberResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.getEmail()).isEqualTo(email);
    }

    private void 로그인_실패_유효_하지_않은_이메일(ExtractableResponse<Response> response) {
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).contains(INVALID_EMAIL);
    }

    private void 로그인_실패_유효_하지_않은_비밀번호(ExtractableResponse<Response> response) {
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).contains(INVALID_PASSWORD);
    }

    private void 로그인_사용자_확인_토큰_불일치_실패(ExtractableResponse<Response> response) {
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).contains(INVALID_TOKEN);
    }

}
