package nextstep.subway.auth.acceptance;

import static nextstep.subway.utils.AuthAcceptanceMethods.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String 계정_이메일 = "pack_c@gmail.com";
    private static final String 계정_패스워드 = "automatic1s";
    private static final String 틀린_패스워드 = "manual1s";
    private static final String 유효하지_않은_토큰 = "abcdefghijklmn";
    private static final int 계정_연령 = 25;

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() {
        //given
        회원_등록되어_있음(계정_이메일, 계정_패스워드, 계정_연령);

        //when
        ExtractableResponse<Response> response = 로그인을_요청한다(계정_이메일, 계정_패스워드);

        //then
        로그인_요청_성공함(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        회원_등록되어_있음(계정_이메일, 계정_패스워드, 계정_연령);
        ExtractableResponse<Response> response = 로그인을_요청한다(계정_이메일, 계정_패스워드);

        //when
        ExtractableResponse<Response> myInfoResponse = 내_정보를_요청한다(extractAccessToken(response));

        //then
        내_정보_요청_성공함(myInfoResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given
        회원_등록되어_있음(계정_이메일, 계정_패스워드, 계정_연령);

        //when
        ExtractableResponse<Response> response = 로그인을_요청한다(계정_이메일, 틀린_패스워드);

        //then
        로그인_요청_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        회원_등록되어_있음(계정_이메일, 계정_패스워드, 계정_연령);
        로그인을_요청한다(계정_이메일, 계정_패스워드);

        //when
        ExtractableResponse<Response> response = 내_정보를_요청한다(유효하지_않은_토큰);

        //then
        내_정보_요청_실패함(response);
    }

}
