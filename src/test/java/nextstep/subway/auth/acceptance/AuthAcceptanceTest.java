package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.AuthSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("인증 관련 기능 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));

        // then
        로그인됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, "password1"));

        // then
        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(new TokenResponse("wrong.token"));

        // then
        토큰_인증_실패됨(response);
    }

    private void 로그인됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 토큰_인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
