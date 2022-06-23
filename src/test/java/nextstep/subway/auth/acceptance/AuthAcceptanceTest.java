package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceResponse.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceResponse.로그인_실패;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

public class AuthAcceptanceTest extends AcceptanceTest {
    private final String 등록된_이메일 = "valid@nextstep.com";
    private final String 유효한_비밀번호 = "password";
    private final String 유요하지않은_비밀번호 = "password!@#";
    private final int 나이 = 20;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(등록된_이메일, 유효한_비밀번호, 나이);
    }

    @DisplayName("계정정보가 유효하다면 로그인 성공한다")
    @Test
    void loginWithValidAccount() {
        ExtractableResponse<Response> response = 로그인_요청(등록된_이메일, 유효한_비밀번호);

        로그인_성공(response);
    }

    @DisplayName("계정정보가 유효하지않다면 로그인 실패한다")
    @Test
    void loginWithInvalidAccount() {
        ExtractableResponse<Response> response = 로그인_요청(등록된_이메일, 유요하지않은_비밀번호);

        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }
}
