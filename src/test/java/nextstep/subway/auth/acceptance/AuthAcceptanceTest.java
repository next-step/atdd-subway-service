package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.step.AuthAcceptanceStep.*;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.member.step.MemberAcceptanceStep.내정보_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth 로그인 하기(토큰 발급 됨)")
    @Test
    void 로그인_요청_토큰발급() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 비밀번호 같지 않음 실패")
    @Test
    void 비밀번호_불일치() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, NEW_PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 로그인 이메일 존재하지 않음 실패")
    @Test
    void 이메일이_존재하지_않음() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(NEW_EMAIL, NEW_PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void 유효하지않은_토큰_실패() {
        // when
        ExtractableResponse<Response> response = 내정보_조회_요청("유효하지않은 토큰");

        // then
        로그인_실패(response);
    }
}
