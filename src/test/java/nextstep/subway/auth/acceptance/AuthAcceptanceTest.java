package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인_요청_성공함;
import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인_요청_실패함;
import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인을_요청;
import static nextstep.subway.auth.acceptance.AuthTestFixture.인증_실패함;
import static nextstep.subway.member.MemberTestFixture.나의_정보_조회_요청;
import static nextstep.subway.member.MemberTestFixture.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@sixthou.kro.kr";
    private static final String PASSWORD = "1q2w3e!@";
    private static final int AGE = 25;


    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // when
        ExtractableResponse<Response> response = 로그인을_요청(EMAIL, PASSWORD);
        // then
        로그인_요청_성공함(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //  Given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String failPassword = "qwerty";
        //  When
        ExtractableResponse<Response> response = 로그인을_요청(EMAIL, failPassword);
        //  Then
        로그인_요청_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //  Given
        TokenResponse tokenResponse = new TokenResponse("test_token_string");
        //  When
        ExtractableResponse<Response> response = 나의_정보_조회_요청(tokenResponse);
        // then
        인증_실패함(response);
    }

}
