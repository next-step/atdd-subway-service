package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(
                MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE
        );

    }


    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = bearer_토큰_인증_요청(
                MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
        );

        // then
        토큰_인증_성공_검증(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = bearer_토큰_인증_요청(MemberAcceptanceTest.EMAIL, "1234");

        // then
        토큰_인증_실패_검증(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        // given
        bearer_토큰_인증_요청(
                MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
        );

        // when
        String accessToken = "wrong-token";
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> response = 내_정보_조회_요청(accessToken, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> bearer_토큰_인증_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all().extract();
        return response;
    }

    public ExtractableResponse<Response> 내_정보_조회_요청(String accessToken, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all().extract();
    }

    private void 토큰_인증_성공_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        String accessToken = response.body().jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();
    }

    private void 토큰_인증_실패_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }
}
