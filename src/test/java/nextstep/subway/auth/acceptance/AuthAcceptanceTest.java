package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.*;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;

@DisplayName("로그인 기능")
class AuthAcceptanceTest extends AcceptanceTest {

    private String email;
    private String password;
    private int age;

    @BeforeEach
    public void setUp() {
        super.setUp();

        email = "email@nextstep.com";
        password = "password";
        age = 30;
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void login() {
        // Given 회원 등록되어 있음
        회원_등록되어_있음(email, password, age);

        // When 로그인 요청
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(email, password);

        // Then 로그인 됨
        로그인_응답_됨(로그인_요청_결과);
        로그인_됨(로그인_요청_결과);
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

}
