package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.내_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        String email = EMAIL;
        String password = PASSWORD;

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 유효하지 않은 이메일")
    @Test
    void myInfoWithBadBearerAuth_invalidEmail() {
        // given
        String email = "invalid-email";
        String password = PASSWORD;

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 유효하지 않은 비밀번호")
    @Test
    void myInfoWithBadBearerAuth_invalidPassword() {
        // given
        String email = EMAIL;
        String password = "invalid-password";

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 토큰 = "invalid-token";

        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(토큰);

        // then
        로그인_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest params = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertThat(response.jsonPath().getString("accessToken")).isNotEmpty()
        );

    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static String 토큰_조회(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.jsonPath().getString("accessToken");
    }
}
