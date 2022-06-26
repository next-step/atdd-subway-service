package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthTestUtils.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @BeforeEach
    void init() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("정상적인 이메일, 비밀번호로 로그인 요청하면 정상 로그인)")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        회원_로그인_성공확인(response);
    }

    @DisplayName("로그인 실패 (존재하지않는 이메일로 로그인 요청하면 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuth_failEmail() {
        // when
        ExtractableResponse<Response> response = 로그인_요청("empty@com", PASSWORD);

        // then
        회원_로그인_실패확인(response);
    }

    @DisplayName("로그인 실패 (일치하지 않는 비밀번호로 로그인 요청하면 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuth_failPassword() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "1234");

        // then
        회원_로그인_실패확인(response);
    }

    @DisplayName("유효하지 않은 토큰 정보로 인증을 하면 요청 실패")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        ExtractableResponse<Response> response = 내_정보_조회_요청(new TokenResponse("InvalidToken"));

        // then
        회원_로그인_실패확인(response);
    }
}
