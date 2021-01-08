package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        final String s = "email@gmail.com";
        final String password = "password";
        AuthAcceptanceSupport.회원_가입_되어있음(s, password);

        ExtractableResponse<Response> 로그인_요청_결과 = AuthAcceptanceSupport.회원_로그인_요청(s, password);

        AuthAcceptanceSupport.로그인_성공함(로그인_요청_결과);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> 로그인_요청_결과 = AuthAcceptanceSupport.회원_로그인_요청("any@gmail.com", "1234");

        AuthAcceptanceSupport.로그인_실패함(로그인_요청_결과);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> 회원기능_요청 = AuthAcceptanceSupport.회원기능_요청("token");

        AuthAcceptanceSupport.인증_실패함(회원기능_요청);
    }
}
