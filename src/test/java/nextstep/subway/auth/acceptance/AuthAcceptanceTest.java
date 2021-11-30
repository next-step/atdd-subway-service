package nextstep.subway.auth.acceptance;

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

import static nextstep.subway.member.MemberAcceptanceTest.토큰생성_상태_회원_정보_조회_요청;
import static nextstep.subway.utils.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "cus@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenRequest tokenRequest = TokenRequest.of(EMAIL, PASSWORD);

        ExtractableResponse<Response> actual = 토큰_발급_요청(tokenRequest);

        토큰_발급됨(actual);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenRequest tokenRequest = TokenRequest.of(EMAIL, "errorPassword");

        ExtractableResponse<Response> actual = 토큰_발급_요청(tokenRequest);

        토큰_발급_실패됨(actual);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse badToken = TokenResponse.of("bad_token");

        ExtractableResponse<Response> actual = 토큰생성_상태_회원_정보_조회_요청(badToken);

        응답_SERVER_ERROR(actual);
    }

    public static ExtractableResponse<Response> 토큰_발급_요청(TokenRequest request) {
        return post("login/token", request);
    }

    public static void 토큰_발급_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 토큰_발급됨(ExtractableResponse<Response> createToken) {
        응답_OK(createToken);
        TokenResponse tokenResponse = createToken.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }

}
