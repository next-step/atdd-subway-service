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

import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어있음;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        회원_등록되어있음("nextstep@github.com", "777", 20);
        //when
        ExtractableResponse<Response> response = 로그인_인증토큰_요청("nextstep@github.com", "777");
        //then
        로그인_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_인증토큰_요청("downstep@github.com", "333");

        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        회원_등록되어있음("nextstep@github.com", "777", 20);
        로그인_인증토큰_요청("nextstep@github.com", "777");

        //when
        ExtractableResponse<Response> response = 유효_토큰_확인("유효하지 않은 토큰");

        토큰_유효하지_않음(response);
    }

    private void 토큰_유효하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 유효_토큰_확인(String token) {
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                auth().oauth2(token).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().extract();
        return response;
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        String accessToken = response.as(TokenResponse.class).getAccessToken();
        assertThat(accessToken).isNotNull();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 로그인_인증토큰_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
        return response;
    }
}
