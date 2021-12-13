package nextstep.subway.auth.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.MemberAcceptanceTestHelper;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTestHelper.회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", "email@email.com");
        params.put("password", "password");

        // when
        ExtractableResponse<Response> response = AuthAcceptanceTestHelper.로그인_요청(params);

        // then
        AuthAcceptanceTestHelper.로그인_요청_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        Map<String, String> invalidPasswordParam = new HashMap<>();
        invalidPasswordParam.put("email", "email@email.com");
        invalidPasswordParam.put("password", "wrong");

        // when
        ExtractableResponse<Response> response = AuthAcceptanceTestHelper.로그인_요청(invalidPasswordParam);

        // then
        AuthAcceptanceTestHelper.로그인_요청_실패(response);

        // given
        Map<String, String> invalidEmailParam = new HashMap<>();
        invalidEmailParam.put("email", "email2@email.com");
        invalidEmailParam.put("password", "password");

        // when
        ExtractableResponse<Response> response2 = AuthAcceptanceTestHelper.로그인_요청(invalidEmailParam);

        // then
        AuthAcceptanceTestHelper.로그인_요청_실패(response2);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

}
