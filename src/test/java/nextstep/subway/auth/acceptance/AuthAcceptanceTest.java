package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethods.*;
import static nextstep.subway.member.MemberAcceptanceMethods.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "testPassword";
    private static final int AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        회원_등록되어_있음(memberRequest);

        // when
        TokenRequest tokenRequest = TokenRequest.of(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 회원_로그인_요청(tokenRequest);

        // then
        회원_로그인_성공함(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        회원_등록되어_있음(memberRequest);

        // when
        String wrongEmail = "wrong@wrong.com";
        String wrongPassword = "wrongPassword";
        TokenRequest tokenRequest = TokenRequest.of(wrongEmail, wrongPassword);

        ExtractableResponse<Response> response = 회원_로그인_요청(tokenRequest);

        // then
        회원_로그인_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @ParameterizedTest
    @ValueSource(strings = {"", "1", "a", "!", "가", "1a!가"})
    void myInfoWithWrongBearerAuth(String wrongToken) {
        // when
        TokenResponse tokenResponse = TokenResponse.from(wrongToken);
        ExtractableResponse<Response> response = 로그인한_회원_정보_조회_요청(tokenResponse);
        // then
        토큰_인증_실패(response);
    }
}
