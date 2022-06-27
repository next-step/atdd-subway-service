package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    //Given 회원 등록되어 있음
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원이_등록되어_있음("email@email.com", "password", 20);

    }

/*
    Feature: 로그인 기능
    Scenario: 로그인을 시도한다.
        Given 회원 등록되어 있음
        When 로그인 요청
        Then 로그인 됨
    */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        final ExtractableResponse<Response> 토근_로그인_응답 = 토큰_요청("email@email.com", "password");

        //then
        로그인_됨(토근_로그인_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        final ExtractableResponse<Response> 잘못된_토큰_요청 = 토큰_요청("email@email.com", "");

        assertThat(잘못된_토큰_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        String token = "유효하지않은 토큰";

        //when
        final ExtractableResponse<Response> 유효하지_않은_토큰_요청 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();

        //then
        assertThat(유효하지_않은_토큰_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }



    public static ExtractableResponse<Response> 토큰_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotEmpty();
    }

    public static TokenResponse 로그인_되어_있음(String email, String password) {
        return 토큰_요청(email, password).as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 회원이_등록되어_있음(String email, String password, int age) {
        return MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }
}
