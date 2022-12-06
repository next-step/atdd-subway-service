package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestUtils.*;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestUtils.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "woowa@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음(EMAIL, PASSWORD, 20);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Background
     *     Given 회원 등록되어 있음
     *
     *   Scenario: 로그인을 시도한다.
     *     When 로그인 요청 (아이디 오입력)
     *     Then 로그인 실패
     *     When 로그인 요청 (비밀번호 오입력)
     *     Then 로그인 실패
     *     When 로그인 요청
     *     Then 로그인 성공
     */
    @Test
    @DisplayName("로그인 요청시 로그인 실패 (아이디 오입력)")
    void loginByWrongId() {
        // when
        ExtractableResponse<Response> response = 로그인_요청("wrong@email.com", PASSWORD);

        // then
        로그인_실패(response);
    }

    @Test
    @DisplayName("로그인 요청시 로그인 실패 (비밀번호 오입력)")
    void loginByWrongPassword() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "wrongPassword");

        // then
        로그인_실패(response);
    }

    @Test
    @DisplayName("로그인 요청시 로그인 성공")
    void loginSuccess() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공(response);
    }

    @Test
    @DisplayName("나의 정보 조회시 유효하지 않은 토큰일 경우 인증 실패")
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 나의_정보_조회_요청("wrongToken");

        // then
        인증_실패(response);
    }
}
