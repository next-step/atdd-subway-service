package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.utils.RequestUtil;
import nextstep.subway.utils.StatusCodeCheckUtil;

public class AuthAcceptanceTest extends AcceptanceTest {
    private String email;
    private String password;
    private int age;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        email = "mslim@naver.com";
        password = "password";
        age = 20;
        MemberAcceptanceTest.회원_등록되어_있음(email, password, age);
    }

    @Test
    void 로그인() {
        // when
        final ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        final String accessToken = 로그인_토큰_발급(email, password);

        // when
        final ExtractableResponse<Response> response = MemberAcceptanceTest.자신의_회원_정보_조회_요청(accessToken);

        // then
        MemberAcceptanceTest.회원_정보_조회됨(response, email, age);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        final ExtractableResponse<Response> response = 로그인_요청(email, "wrongPassword");

        // then
        StatusCodeCheckUtil.unauthorized(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        final ExtractableResponse<Response> response = MemberAcceptanceTest.자신의_회원_정보_조회_요청("invalidToken");

        // then
        StatusCodeCheckUtil.unauthorized(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RequestUtil.post("/login/token", params);
    }

    public static void 로그인_됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
        assertThat(response.jsonPath().getObject("accessToken", String.class)).isNotNull();
    }

    public static String 로그인_토큰_발급(final String email, final String password) {
        final ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.jsonPath().getObject("accessToken", String.class);
    }
}
