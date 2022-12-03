package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_실패됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private final String EMAIL = "email@email.com";
    private final String WRONG_EMAIL = "wrong@email.com";
    private final String PASSWORD = "password";
    private final String WRONG_PASSWORD = "wrong_password";
    private final int AGE = 20;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("Bearer Auth")
    @Test
    void loginSuccess() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_성공됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void loginFail() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_실패됨(response);
    }

    @DisplayName("잘못된 email로 로그인 실패")
    @Test
    void loginFailWithWrongEmail() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(WRONG_EMAIL, PASSWORD);

        로그인_실패됨(response);
    }

    @DisplayName("잘못된 password로 로그인 실패")
    @Test
    void loginFailWithWrongPassword() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);

        로그인_실패됨(response);
    }
}
