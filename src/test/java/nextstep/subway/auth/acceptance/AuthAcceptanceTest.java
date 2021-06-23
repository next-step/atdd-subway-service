package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

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

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String WRONG_PASSWORD = "wrong_password";
    public static final String INVALID_TOKEN = "invalid_token";

    @BeforeEach
    public void setup() {
        super.setUp();
        회원_가입을_한다(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("이메일과 비밀번호로 로그인을 한다.")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공함(response);
    }

    @DisplayName("틀린 비밀번호로 로그인을 한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);

        // then
        로그인_실패됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 나의 정보를 조회한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        TokenResponse invalidToken = new TokenResponse(INVALID_TOKEN);

        // when
        ExtractableResponse<Response> response = 나의_정보_조회_요청(invalidToken);

        // then
        회원_인증_실패됨(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        // when
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }

    public static void 로그인_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 회원_인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인을_한다(String email, String password) {
        return 로그인_요청(email, password);
    }
}
