package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthSteps.*;
import static nextstep.subway.member.MemberSteps.*;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String INVALID_TOKEN = "invalid_token";

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        베어러_인증_성공(response);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(INVALID_TOKEN);

        베어러_인증_실패(response);
    }
}
