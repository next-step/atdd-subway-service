package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("토큰 발급(로그인) 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String WRONG_PASSWORD = "wrong_password";
    public static final String INVALID_TOKEN = "invalid_token";

    @BeforeEach
    public void setup() {
        super.setUp();
        회원_등록_되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("이메일과 비밀번호로 토큰 발급을 요청을 한다.")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰_발급_요청(EMAIL, PASSWORD);

        // then
        토큰_발급_성공(response);
    }

    @DisplayName("틀린 비밀번호로 토큰 발급을 요청을 한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰_발급_요청(EMAIL, WRONG_PASSWORD);

        // then
        토큰_발급_실패됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 나(현재 로그인 회원) 의 정보를 조회한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        토큰_발급_요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 나의_정보_조회_요청(INVALID_TOKEN);

        // then
        토큰_인증_실패됨(response);
    }

    public static ExtractableResponse<Response> 토큰_발급_요청(String email, String password) {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }

    public static void 토큰_발급_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    public static void 토큰_발급_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
