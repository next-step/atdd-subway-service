package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.step.AuthStep.*;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String 이메일 = "email@email.com";
    private static final String 비밀번호 = "password";
    private static final int 나이 = 10;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_등록되어_있음(이메일, 비밀번호, 나이);

        // when
        ExtractableResponse<Response> response = 로그인_요청(이메일, 비밀번호);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

}
