package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "programmer-sjk@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_EMAIL = "fake@email.com";
    private static final String WRONG_PASSWORD = "wrong_password";

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_생성을_요청(EMAIL, PASSWORD, 33);
    }

    @DisplayName("정상적으로 로그인을 할 수 있다")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공(response);
    }

    @DisplayName("잘못된 이메일로 로그인할 경우 실패한다")
    @Test
    void myInfoWithWrongEmailAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(WRONG_EMAIL, PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("잘못된 패스워드로 로그인할 경우 실패한다")
    @Test
    void myInfoWithWrongPasswordAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("유효하지 않은 토큰으로 로그인할 경우 실패한다")
    @Test
    void myInfoWithWrongBearerAuth() {
        // Todo 내 정보 조회 기능 개발 후 테스트 추가
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        유효하지_않은_토큰(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String pw) {
        TokenRequest tokenRequest = new TokenRequest(email, pw);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(tokenResponse.getAccessToken()).isNotEmpty()
        );
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 유효하지_않은_토큰(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
