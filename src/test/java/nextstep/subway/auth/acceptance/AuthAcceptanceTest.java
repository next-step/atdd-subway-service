package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    String email;
    String password;

    @BeforeEach
    void setup() {
        email = "test@email.com";
        password = "test";
        회원가입_요청(new MemberRequest(email, password, 21));
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(email, password));

        로그인_성공함(response);
    }

    @DisplayName("존재하지 않는 이메일로 로그인을 요청하면 실패한다")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest("invalid@email.com", password));

        로그인_실패함(response);
    }

    @DisplayName("패스워드가 같지않으면 실패한다")
    @Test
    void 로그인_실패_패스워드() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(email, "wrong password"));

        로그인_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        String wrongToken = "wrongToken";
        ExtractableResponse<Response> response = 회원정보_요청(wrongToken);

        로그인_실패함(response);
    }

    private static ExtractableResponse<Response> 회원가입_요청(MemberRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/members")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 로그인_요청(TokenRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 회원정보_요청(String token) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    private static void 로그인_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
