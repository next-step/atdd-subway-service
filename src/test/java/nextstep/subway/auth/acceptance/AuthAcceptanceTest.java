package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestAssuredRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/login/token";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private MemberResponse member;

    @BeforeEach
    public void setUp() {
        super.setUp();
        this.member = 회원_생성_및_회원_조회(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> 회원_로그인_요청_응답 = 회원_로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공됨(회원_로그인_요청_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> 회원_로그인_요청_응답 = 회원_로그인_요청(EMAIL, PASSWORD + "wrong");

        // then
        로그인_실패됨(회원_로그인_요청_응답);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 토큰 = 로그인_요청_및_토큰_추출(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> 잘못된_토큰으로_요청_응답 = 내_정보_조회(토큰 + "wrong");

        // then
        잘못된_토큰으로_요청됨(잘못된_토큰으로_요청_응답);
    }

    public static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body, null);
    }

    public void 로그인_성공됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.body().jsonPath().getString("accessToken")).isNotNull();
    }

    public void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    public void 잘못된_토큰으로_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    public static String 로그인_요청_및_토큰_추출(String email, String password) {
        return 회원_로그인_요청(email, password).as(TokenResponse.class)
                .getAccessToken();
    }

    public static String 신규_회원가입_후_로그인_토큰_추출(String email, String password, int age) {
        회원_생성을_요청(email, password, age);
        return 로그인_요청_및_토큰_추출(email, password);
    }
}
