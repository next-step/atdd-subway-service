package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.*;
import static nextstep.subway.member.MemberAcceptanceStep.회원_생성을_요청;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "koreatech93@naver.com";
    public static final String PASSWORD = "123123";

    @BeforeEach
    public void authSetUp() {
        회원_생성을_요청(EMAIL, PASSWORD, 30);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        인증_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "12345");

        // then
        인증_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청("koreatech94@naver.com", "124123");

        // then
        인증_실패(response);
    }
}
