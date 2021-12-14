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
        password = "12345678";
        age = 20;
        회원_등록되어_있음(email, password, age);
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
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private static void 회원_등록되어_있음(final String email, final String password, final int age) {
        MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }

    private static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RequestUtil.post("/login/token", params);
    }

    private static void 로그인_됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
        assertThat(response.jsonPath().getObject("accessToken", String.class)).isNotNull();
    }
}
