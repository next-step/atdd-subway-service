package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestHelper.*;
import static nextstep.subway.member.MemberAcceptanceTestHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;

@DisplayName("인증 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private static String EMAIL = "changchang743@gmail.com";
    private static String PASSWORD = "1234";
    private static int AGE = 29;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인 성공 - 토큰 발급")
    @Test
    void loginAndGetToken() {
        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        //then
        로그인_됨(로그인_응답);
    }

    @DisplayName("내 정보 조회 (Bearer Auth)")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 토큰_정보(로그인_응답);

        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청(토큰);

        // then
        내_정보_조회_됨(내_정보_조회_응답, EMAIL, AGE);
    }

    @DisplayName("등록되어 있지 않은 이메일 로그인 시도 (Bearer Auth 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuthToWrongEmail() {
        // given
        String wrongEmail = "wrongEmail@gmail.com";

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(wrongEmail, PASSWORD);

        // then
        로그인_실패됨(로그인_응답);
    }

    @DisplayName("비밀번호가 일치 않은 로그인 시도 (Bearer Auth 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuthToWrongPassword() {
        // given
        String wrongPassword = "wrongPassword";

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, wrongPassword);

        // then
        로그인_실패됨(로그인_응답);
    }

    @DisplayName("유효하지 않은 토큰으로 내 정보 조회 (Bearer Auth 유효하지 않은 토큰)")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청("wrongToken");

        // then
        인증_실패됨(내_정보_조회_응답);
    }

}
