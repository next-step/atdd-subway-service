package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.유효성_인증_실패;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestFixture.내_정보_조회_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestFixture.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_EMAIL = "wrongemail@email.com";
    private static final String WRONG_PASSWORD = "wrongpassword";

    @BeforeEach
    void setup() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD, 30);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(WRONG_EMAIL, WRONG_PASSWORD);

        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = 내_정보_조회_요청("wrongAccessToken");

        유효성_인증_실패(response);
    }
}
