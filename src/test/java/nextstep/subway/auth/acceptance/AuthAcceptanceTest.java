package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthSteps.*;
import static nextstep.subway.member.MemberSteps.로그인_요청;
import static nextstep.subway.member.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email";
    public static final String CHILDREN_EMAIL = "children_email";
    public static final String TEENAGER_EMAIL = "teenager_email";
    public static final String PASSWORD = "password";
    public static final String CHILDREN_PASSWORD = "children_password";
    public static final String TEENAGER_PASSWORD = "teenager_password";
    public static final int AGE = 20;
    public static final int TEENAGER_AGE = 15;
    public static final int CHILDREN_AGE = 11;
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String INVALID_EMAIL = "em";
    public static final String INVALID_PASSWORD = "pw";

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(memberA);

        베어러_인증_성공(response);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(INVALID_EMAIL, INVALID_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(INVALID_TOKEN);

        베어러_인증_실패(response);
    }
}
