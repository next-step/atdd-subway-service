package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private final String memberEmail = "abc@com";
    private final String passString = "1234";

    @BeforeEach
    void init() {
        회원_생성을_요청(memberEmail, passString, 20);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 회원_로그인을_시도한다(memberEmail, passString);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 (이메일 오류)")
    @Test
    void myInfoWithBadBearerAuthFailEmail() {
        ExtractableResponse<Response> response = 회원_로그인을_시도한다("empty@com", passString);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 (암호 오류)")
    @Test
    void myInfoWithBadBearerAuthFailPassWord() {
        ExtractableResponse<Response> response = 회원_로그인을_시도한다(memberEmail, "empty");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public static ExtractableResponse<Response> 회원_로그인을_시도한다(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
}
