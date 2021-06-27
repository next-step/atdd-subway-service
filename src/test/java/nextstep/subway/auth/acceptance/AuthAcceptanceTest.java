package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.application.AuthServiceTest;
import nextstep.subway.auth.dto.TokenResponse;
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

        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        
        로그인_됨(tokenResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 등록되지 않은 회원 정보로 로그인시")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

}
