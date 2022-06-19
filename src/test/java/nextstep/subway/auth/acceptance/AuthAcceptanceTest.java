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
    public static final String MEMBER_EMAIL = "abc@com";
    public static final String PASSWORD = "1234";

    @BeforeEach
    void init() {
        //when
        회원_생성을_요청(MEMBER_EMAIL, PASSWORD, 20);
    }

    @DisplayName("Bearer Auth (정상적인 이메일, 비밀번호로 로그인요청하면 정상 로그인)")
    @Test
    void myInfoWithBearerAuth() {
        //given
        ExtractableResponse<Response> response = 회원_로그인을_시도한다(MEMBER_EMAIL, PASSWORD);
        //then
        회원_로그인_성공확인(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 (존재하지않는 이메일로 로그인 요청하면 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuthFailEmail() {
        //given
        ExtractableResponse<Response> response = 회원_로그인을_시도한다("empty@com", PASSWORD);
        //then
        회원_로그인_실패확인(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 (틀린 비밀번호로 로그인 요청하면 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuthFailPassWord() {
        //given
        ExtractableResponse<Response> response = 회원_로그인을_시도한다(MEMBER_EMAIL, "empty");
        //then
        회원_로그인_실패확인(response);
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

    public static void 회원_로그인_성공확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_로그인_실패확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
