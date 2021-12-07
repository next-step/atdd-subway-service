package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final TokenResponse BAD_TOKEN = TokenResponse.from("BAD_TOKEN");
    private static final String BAD_PASSWORD = "BAD_PASSWORD";

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given : 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void performScenario() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response1 = 로그인_요청(EMAIL, PASSWORD);
        로그인_됨(response1);

        ExtractableResponse<Response> response2 = 토큰을_통한_인증(response1.as(TokenResponse.class));
        토큰_인증_됨(response2);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, BAD_PASSWORD);
        로그인_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = 토큰을_통한_인증(BAD_TOKEN);
        토큰_인증_실패함(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(TokenRequest.of(email, password))
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.as(TokenResponse.class)
            .getAccessToken())
            .isNotBlank();
    }

    private static ExtractableResponse<Response> 토큰을_통한_인증(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    private static void 토큰_인증_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(Member.class).getEmail()).isEqualTo(EMAIL);
        assertThat(response.as(Member.class).getAge()).isEqualTo(AGE);
    }

    private static void 토큰_인증_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
