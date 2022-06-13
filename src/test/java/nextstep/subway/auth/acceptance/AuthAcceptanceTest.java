package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.내정보_조회_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.내정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private String EMAIL = "toughchb@gmail.com";
    private String PASSWORD = "password";
    private String WRONG_PASSWORD = "wrong_password";
    private int AGE = 18;

    private String WRONG_TOKEN = "wrong_token";

    @BeforeEach
    void before() {
        //given: 회원 등록 되어 있음
        super.setUp();
        ExtractableResponse<Response> 회원_생성_요청 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_요청);
    }

    @DisplayName("Bearer Auth 정상 로그인")
    @Test
    void myInfoWithBearerAuth() {
        //when: 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        //then: 로그인 됨
        로그인_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {

        //when: 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);

        //then: 로그인 실패
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        //when: 유효하지 않은 토큰으로 요청
        ExtractableResponse<Response> 내정보_조회_요청_응답 = 내정보_조회_요청(WRONG_TOKEN);

        //then: 요천 실패
        내정보_조회_실패(내정보_조회_요청_응답);
    }
}
