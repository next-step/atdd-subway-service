package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공후_토큰_조회됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_시도함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.정보가_달라_로그인_실패됨;
import static nextstep.subway.member.MemberAcceptanceSupport.나의_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceTest extends AcceptanceTest {
    private String 등록된_이메일;
    private String 등록된_패스워드;
    private int age;
    private String 잘못된_이메일;
    private String 잘못된_패스워드;
    private String 유효하지_않은_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        등록된_이메일 = "drogba02@naver.com";
        등록된_패스워드 = "password";
        age = 29;
        잘못된_이메일 = "invalid@google.com";
        잘못된_패스워드 = "passwordInvalid";
        유효하지_않은_토큰 = "invalid_token";

        회원_생성을_요청(등록된_이메일, 등록된_패스워드, age);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
     * */
    @DisplayName("계정, 패스워드가 모두 일치하면 로그인에 성공한다")
    @Test
    void success_login_test() {
        ExtractableResponse<Response> response = 로그인_시도함(등록된_이메일, 등록된_패스워드);

        로그인_성공됨(response);
    }

    @DisplayName("계정이 일치하지 않으면 로그인에 실패한다")
    @Test
    void invalid_email_login_test() {
        ExtractableResponse<Response> response = 로그인_시도함(잘못된_이메일, 등록된_패스워드);

        정보가_달라_로그인_실패됨(response);
    }

    @DisplayName("패스워드가 일치하지 않으면 로그인에 실패한다")
    @Test
    void invalid_password_login_test() {
        ExtractableResponse<Response> response = 로그인_시도함(등록된_이메일, 잘못된_패스워드);

        정보가_달라_로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효한 토큰")
    @Test
    void myInfoWithCorrectBearerAuth() {
        ExtractableResponse<Response> loginResponse = 로그인_시도함(등록된_이메일, 등록된_패스워드);
        String accessToken = 로그인_성공후_토큰_조회됨(loginResponse);

        ExtractableResponse<Response> response = 나의_정보_조회_요청(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = 나의_정보_조회_요청(유효하지_않은_토큰);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
