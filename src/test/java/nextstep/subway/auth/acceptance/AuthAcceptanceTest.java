package nextstep.subway.auth.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.MemberAcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, 10);

        Map<String, String> params = new HashMap<>();
        params.put("email", MemberAcceptanceTest.EMAIL);
        params.put("password", MemberAcceptanceTest.PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰_발급을_요청(params);

        // then
        토큰_발급_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", MemberAcceptanceTest.EMAIL);
        params.put("password", MemberAcceptanceTest.PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰_발급을_요청(params);

        // then
        토큰_발급_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .auth().oauth2("test123")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/members/me")
            .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 토큰_발급을_요청(Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/login/token")
            .then().log().all().extract();
        return response;
    }

    private void 토큰_발급_성공(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 토큰_발급_실패(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
