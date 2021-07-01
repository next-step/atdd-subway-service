package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

@SuppressWarnings("NonAsciiCharacters")
public class AuthAcceptanceTest extends AcceptanceTest {

    private final String email = "test@test.com";
    private final String password = "1111";
    private final String incorrectPassword = "2222";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final Integer age = 20;

        // when
        final ExtractableResponse<Response> response = 회원_생성을_요청(email, password, age);

        // then
        회원_생성됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        final ExtractableResponse<Response> loginResponse = 로그인_요청(email, password);

        // then
        로그인_됨(loginResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        final ExtractableResponse<Response> loginResponse = 로그인_요청(email, incorrectPassword);

        // then
        로그인_실패됨(loginResponse);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        final TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 로그인_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    private void 로그인_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
