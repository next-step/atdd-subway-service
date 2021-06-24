package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

public class AuthAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_되어있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("login")
    @Test
    void login() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인됨(response);
        토근발급됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        TokenResponse 토큰 = 로그인되어있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 나의정보_조회_요청(토큰);

        //then
        나의정보_조회됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청("badEmail", "badPassword");

        //then
        인증실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        로그인되어있음(EMAIL, PASSWORD);
        TokenResponse 유효하지않은토큰 = new TokenResponse("유효하지않은토큰");

        //when
        ExtractableResponse<Response> response = 나의정보_조회_요청(유효하지않은토큰);

        //then
        인증실패됨(response);
    }

    public static TokenResponse 로그인되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        로그인됨(response);
        토근발급됨(response);
        return response.as(TokenResponse.class);
    }

    public static void 토근발급됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    public static void 로그인됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
        return response;
    }

    private void 인증실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
