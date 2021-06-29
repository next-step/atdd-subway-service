package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.application.AuthServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.AuthSteps.*;
import static nextstep.subway.member.MemeberSteps.*;

public class AuthAcceptanceTest extends AcceptanceTest {
    private String EMAIL = AuthServiceTest.EMAIL;
    private String PASSWORD = AuthServiceTest.PASSWORD;
    private int AGE = AuthServiceTest.AGE;

    @DisplayName("Bearer Auth - 로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {
        회원_등록_되어_있음(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        TestToken token = response.as(TestToken.class);

        로그인_됨(response);
        내_회원_정보_조회됨(token);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 등록되지 않은 회원 정보로 로그인시")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰1 - 이상한 값(파싱할때 예외발생하는 경우)")
    @Test
    void myInfoWithWrongBearerAuth1() {
        TestToken token = new TestToken("ABC.ABC.123");

        ExtractableResponse<Response> response = 내_정보_조회_요청_유효하지_않은_토큰(token);

        내_정보_조회_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰2 - 만료시간 지난 토큰")
    @Test
    void myInfoWithWrongBearerAuth2() {
        회원_등록_되어_있음(EMAIL, PASSWORD, AGE);
        String initToken = 만료기간_지난_토큰_발급_요청(EMAIL, PASSWORD);
        TestToken testToken = new TestToken(initToken);

        ExtractableResponse<Response> response = 내_정보_조회_요청_유효하지_않은_토큰(testToken);

        내_정보_조회_실패됨(response);
    }
}
