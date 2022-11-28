package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.인증_실패;
import static nextstep.subway.member.MemberAcceptanceTestFixture.내_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTestFixture.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private final String email = "valid@email.com";
    private final String password = "valid_password";
    private final String invalidEmail = "invalid@email.com";
    private final String invalidPassword = "invalid_password";

    @BeforeEach
    void authSetUp() {
        회원_생성을_요청(email, password, 26);
    }

    /**
     *  Given 등록되어 있는 이메일과 비밀번호로
     *  When 로그인을 시도 할 경우
     *  Then 로그인에 성공한다.
     */
    @DisplayName("등록된 이메일과 비밀번호로 로그인 시 로그인에 성공한다.")
    @Test
    void login() {
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        로그인_성공(response);
    }

    /**
     *  Given 등록되어 있지 않은 이메일과 비밀번호로
     *  When 로그인을 시도 할 경우
     *  Then 로그인에 실패한다.
     */
    @DisplayName("등록되지 않은 이메일과 비밀번호로 로그인 시 로그인에 실패한다.")
    @Test
    void loginFailed() {
        ExtractableResponse<Response> response = 로그인_요청(invalidEmail, invalidPassword);

        로그인_실패(response);
    }

    /**
     *  Given 등록된 비밀번호와 다른 비밀번호로
     *  When 로그인을 시도 할 경우
     *  Then 로그인에 실패한다.
     */
    @DisplayName("등록된 비밀번호와 다른 비밀번호로 로그인 시 로그인에 실패한다.")
    @Test
    void loginFailedWithDifferentPassword() {
        ExtractableResponse<Response> response = 로그인_요청(email, invalidPassword);

        로그인_실패(response);
    }

    /**
     *  Given 유효하지 않은 토큰으로
     *  When 인증이 필요한 기능을 요청 할 경우
     *  Then 인증에 실패한다.
     */
    @DisplayName("유효하지 않은 토큰으로 인증이 필요한 기능을 요청 시 인증에 실패한다.")
    @Test
    void invalidToken() {
        String invalidToken = "invalid_token";

        ExtractableResponse<Response> response = 내_정보_조회_요청(invalidToken);

        인증_실패(response);
    }
}
