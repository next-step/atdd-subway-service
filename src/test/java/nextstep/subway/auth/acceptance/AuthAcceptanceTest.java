package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("로그인 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then 로그인 됨
        로그인_성공함(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // When: 로그인 요청 shinmj@email.com/password
        ExtractableResponse<Response> emailFailResponse = 로그인_요청("shinmj@email.com", PASSWORD);

        // Then: 로그인 실패
        로그인_실패함(emailFailResponse);

        // When: 로그인 요청 email@email.com/password11
        ExtractableResponse<Response> passwordFailResponse = 로그인_요청(EMAIL, "password11");
        // Then: 로그인 실패
        로그인_실패함(passwordFailResponse);

        // When: 로그인 요청 shinmj@email.com/password11
        ExtractableResponse<Response> response = 로그인_요청("shinmj@email.com", "password11");
        // Then: 로그인 실패
        로그인_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When: 내정보 조회 요청 Bearer invalidToken
        ExtractableResponse<Response> response = 내정보_조회_요청("invalidToken");

        // Then: 조회 실패
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static void 로그인_성공함(ExtractableResponse<Response> response) {
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertNotNull(response.body().as(TokenResponse.class));
        });
    }

    public static void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static String 토큰발급(ExtractableResponse<Response> response) {
        return response
            .body().as(TokenResponse.class)
            .getAccessToken();
    }

}
