package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.내_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String email = "test@nextstep.com";
        String password = "password";
        Integer age = 30;

        // given
        회원_등록되어_있음(email, password, age);

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_요청_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패(회원가입 안함)")
    @Test
    void myInfoWithBadBearerAuth() {
        String email = "test@nextstep.com";
        String password = "password";

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_요청_실패(response);
    }

    @DisplayName("Bearer Auth 로그인 실패(패스워드 오타)")
    @Test
    void myInfoWithInvalidPassword() {
        String email = "test@nextstep.com";
        String password = "password";
        Integer age = 30;
        String invalidPassword = "wrongPassword";

        // given
        회원_등록되어_있음(email, password, age);

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, invalidPassword);

        // then
        로그인_요청_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        String invalidToken = "this token is so bad";

        ExtractableResponse<Response> response = 내_정보_조회_요청(invalidToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static String 로그인_됨(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        로그인_요청_성공(response);

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();

        return response;
    }

    public static void 로그인_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        String token = tokenResponse.getAccessToken();
        assertThat(token).isNotEmpty();
    }

    public static void 로그인_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 로그인_실패함(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        로그인_요청_실패(response);
    }
}
