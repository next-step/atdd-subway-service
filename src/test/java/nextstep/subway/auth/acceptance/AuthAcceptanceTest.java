package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.application.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.utils.AuthRestAssuredTestUtils.*;
import static nextstep.subway.utils.MemberRestAssuredUtils.*;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberService memberService;

    private String 이메일주소;
    private String 등록되지_않은_이메일주소;
    private String 패스워드;
    private String 토큰;
    private String 잘못된_토큰;
    private int 나이;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        이메일주소 = "email@email.com";
        등록되지_않은_이메일주소 = "jack@email.com";
        패스워드 = "password";
        잘못된_토큰 = "";
        나이 = 32;
    }

    @DisplayName("로그인을 시도한다")
    @Test
    void login() {
        // given
        회원_등록됨(이메일주소, 패스워드, 나이);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(이메일주소, 패스워드);

        // then
        토큰 = 로그인_됨(loginResponse);

        // when
        ExtractableResponse<Response> failLoginResponse = 잘못된_정보로_로그인을_요청(등록되지_않은_이메일주소, 패스워드);
        // then
        로그인_실패됨(failLoginResponse);

        // when
        ExtractableResponse<Response> myInfoResponse = 나의_정보_조회를_요청(토큰);
        // then
        나의_정보가_조회됨(myInfoResponse);

        // when
        ExtractableResponse<Response> myInfoFailResponse = 나의_정보_조회를_요청(잘못된_토큰);
        // then
        나의_정보가_조회되지_않음(myInfoFailResponse);
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
